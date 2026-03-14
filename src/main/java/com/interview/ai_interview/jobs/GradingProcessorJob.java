package com.interview.ai_interview.jobs;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interview.ai_interview.dto.request.GradingResultRequest;
import com.interview.ai_interview.dto.request.GradingResultRequest.QuestionResult;
import com.interview.ai_interview.dto.response.GradingResponse;
import com.interview.ai_interview.dto.response.GradingResponse.InterviewInfo;
import com.interview.ai_interview.models.Answer;
import com.interview.ai_interview.models.GradingJob;
import com.interview.ai_interview.models.GradingStatusEnum;
import com.interview.ai_interview.models.InterviewParticipant;
import com.interview.ai_interview.models.TranscriptStatusEnum;
import com.interview.ai_interview.dto.response.PendingAnswerResponse;
import com.interview.ai_interview.models.CategoryScore;
import com.interview.ai_interview.repositories.AnswerRepository;
import com.interview.ai_interview.repositories.CategoryScoreRepository;
import com.interview.ai_interview.repositories.GradingJobRepository;
import com.interview.ai_interview.repositories.InterviewParticipantRepository;
import com.interview.ai_interview.services.GeminiClient;
import com.interview.ai_interview.utils.PromptingGradingAndRecomandating;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class GradingProcessorJob {

    private final GeminiClient geminiClient;
    private final ObjectMapper objectMapper;
    private final AnswerRepository answerRepository;
    private final InterviewParticipantRepository participantRepository;
    private final GradingJobRepository gradingJobRepository;
    private final CategoryScoreRepository categoryScoreRepository;

    public void processAllPendingGrading() {
        List<PendingAnswerResponse> allPending = answerRepository.findAllAnswersForPendingJobs();

        if (allPending.isEmpty()) {
            System.out.println("No pending grading jobs found.");
            return;
        }

        // group by jobId
        Map<UUID, List<PendingAnswerResponse>> groupedByJob = allPending.stream()
                .collect(Collectors.groupingBy(PendingAnswerResponse::getJobId));

        groupedByJob.forEach((jobId, answers) -> {
            System.out.println("Processing Job ID: " + jobId);
            List<GradingResponse> gradings = new ArrayList<>();

            // Kirim answers (list pertanyaan & jawaban) ke AI
            for (PendingAnswerResponse data : answers) {
                InterviewInfo interview = InterviewInfo.builder()
                        .id(data.getInterview().getId().toString())
                        .name(data.getInterview().getName())
                        .context(data.getInterview().getContext())
                        .objective(data.getInterview().getObjective())
                        .roleTarget(data.getInterview().getRoleTarget())
                        .levelTarget(data.getInterview().getLevelTarget())
                        .technology(data.getInterview().getTechnology())
                        .purpose(data.getInterview().getPurpose().toString())
                        .build();
                gradings.add(GradingResponse.builder()
                        .answerId(data.getAnswerId().toString())
                        .interview(interview)
                        .question(data.getQuestionText())
                        .answer(data.getTranscript())
                        .build());
            }
            try {
                String jsonInput = objectMapper.writeValueAsString(gradings);
                String aiRawResponse = geminiClient.evaluate(
                        PromptingGradingAndRecomandating.buildFullPrompt(jsonInput),
                        PromptingGradingAndRecomandating.getExpectedOutputFormat());
                var cleanedJson = cleanJsonOutput(aiRawResponse);
                System.out.println("AI Response: " + cleanedJson);

                GradingResultRequest gradingResult = objectMapper.readValue(cleanedJson, GradingResultRequest.class);

                saveGradingResult(gradingResult, answers.get(0).getParticipantId(), jobId);
            } catch (Exception e) {
                handleRetying(jobId);
                log.error("Error occurred while processing grading for job ID: {}", jobId, e);
            }
        });
    }

    @Transactional
    public void saveGradingResult(GradingResultRequest result, UUID participantId, UUID jobId) {
        
        // 1. Ambil score kategori dari tabel category_scores
        List<CategoryScore> categories = categoryScoreRepository.findAll();
        Map<String, Float> scoreMap = categories.stream()
                .collect(Collectors.toMap(
                        cs -> cs.getCategory().toLowerCase().trim(),
                        CategoryScore::getScore));

        float scoreTF = scoreMap.getOrDefault("technical fundamental", 50f);
        float scorePS = scoreMap.getOrDefault("problem solving", 30f);
        float scoreComm = scoreMap.getOrDefault("communication", 20f);
        float totalscore = scoreTF + scorePS + scoreComm;

        // 2. Update Score per Jawaban & hitung weighted score
        float totalWeightedScore = 0f;
        int answerCount = 0;

        for (QuestionResult res : result.getQuestionResults()) {
            Answer updatedAnswer = answerRepository.findById(UUID.fromString(res.getAnswerId())).orElseThrow();
            updatedAnswer.setTechnicalFundamentalScore(res.getTechnicalFundamentalScore());
            updatedAnswer.setProblemSolvingScore(res.getProblemSolvingScore());
            updatedAnswer.setCommunicationScore(res.getCommunicationScore());
            updatedAnswer.setStatus(TranscriptStatusEnum.DONE);

            answerRepository.save(updatedAnswer);

            // Hitung weighted score per answer
            float tf = res.getTechnicalFundamentalScore() != null ? res.getTechnicalFundamentalScore() : 0f;
            float ps = res.getProblemSolvingScore() != null ? res.getProblemSolvingScore() : 0f;
            float comm = res.getCommunicationScore() != null ? res.getCommunicationScore() : 0f;

            float weightedScore = ((tf * scoreTF) + (ps * scorePS) + (comm * scoreComm)) / totalscore;
            totalWeightedScore += weightedScore;
            answerCount++;
        }

        // 3. Hitung rata-rata weighted score
        float averageScore = answerCount > 0 ? totalWeightedScore / answerCount : 0f;

        // 4. Update Total Score di Participant
        InterviewParticipant participant = participantRepository.findById(participantId).orElseThrow();
        participant.setTotalScore(averageScore);
        participant.setRecommendation(
                result.getRecommendation().getDecision());
        participant.setSummaryReason(result.getRecommendation().getReason());
        participant.setFinishedAt(java.time.LocalDateTime.now());

        participantRepository.save(participant);

        // 3. Update Status Grading Job
        GradingJob job = gradingJobRepository.findById(jobId).orElseThrow();
        job.setStatus(GradingStatusEnum.DONE);

        gradingJobRepository.save(job);
        System.out.println("Grading results for participant " + participantId + " successfully saved.");
    }

    public void handleRetying(UUID jobId) {
        GradingJob job = gradingJobRepository.findById(jobId).orElseThrow();
        int retry = job.getRetryCount() == null ? 0 : job.getRetryCount();

        if (retry < 3) {
            job.setRetryCount(retry + 1);
            job.setStatus(GradingStatusEnum.PENDING);
        } else {
            job.setStatus(GradingStatusEnum.FAILED);
        }
        gradingJobRepository.save(job);
    }

    private String cleanJsonOutput(String output) {
        if (output == null)
            return "{}";

        // Hapus opening ```json atau ```
        output = output.replaceAll("^```json", "");
        output = output.replaceAll("^```", "");

        // Hapus closing ```
        output = output.replaceAll("```$", "");

        return output.trim();
    }
}