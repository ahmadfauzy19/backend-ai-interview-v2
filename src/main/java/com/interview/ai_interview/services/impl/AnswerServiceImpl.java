package com.interview.ai_interview.services.impl;

import com.interview.ai_interview.dto.response.CandidateListProjection;
import com.interview.ai_interview.dto.response.CandidateResultResponse;
import com.interview.ai_interview.dto.response.CandidateSummaryResponse;
import com.interview.ai_interview.dto.response.QuestionAnswerResponse;
import com.interview.ai_interview.models.Answer;
import com.interview.ai_interview.models.Candidate;
import com.interview.ai_interview.models.Interview;
import com.interview.ai_interview.models.InterviewParticipant;
import com.interview.ai_interview.models.Question;
import com.interview.ai_interview.models.TranscriptStatusEnum;
import com.interview.ai_interview.repositories.AnswerRepository;
import com.interview.ai_interview.repositories.CandidateRepository;
import com.interview.ai_interview.repositories.InterviewParticipantRepository;
import com.interview.ai_interview.repositories.QuestionRepository;
import com.interview.ai_interview.services.AnswerService;
import com.interview.ai_interview.services.MinioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.UUID;
import java.io.File;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnswerServiceImpl implements AnswerService {

    private final MinioService minioService;
    private final QuestionRepository questionRepository;
    private final CandidateRepository candidateRepository;
    private final InterviewParticipantRepository participantRepository;
    private final AnswerRepository answerRepository;

    @Override
    @Transactional
    public void uploadAnswer(MultipartFile file, UUID questionId, UUID userId, String breakTime, String answerTime) {
        File tempVideo = null;
        File tempAudio = null;
        try {
                Question question = questionRepository.findById(questionId)
                        .orElseThrow(() -> new RuntimeException("Question not found"));

                Candidate candidate = candidateRepository.findByUserId(userId)
                        .orElseThrow(() -> new RuntimeException("Candidate not found"));

                Interview interview = question.getInterview();

                InterviewParticipant participant = participantRepository
                        .findByInterviewIdAndCandidateId(interview.getId(), candidate.getId())
                        .orElseGet(() -> {
                                InterviewParticipant newParticipant = InterviewParticipant.builder()
                                        .interview(interview)
                                        .candidate(candidate)
                                        .startedAt(LocalDateTime.now())
                                        .build();
                                return participantRepository.save(newParticipant);
                        });

                // Upload ke MinIO
                String storedFileName = minioService.uploadFile(file);

                // Save Answer tanpa transcript
                Answer answer = Answer.builder()
                        .participant(participant)
                        .question(question)
                        .audioPath(storedFileName)
                        .breakTime(breakTime)
                        .answerTime(answerTime)
                        .status(TranscriptStatusEnum.PENDING)
                        .retryCount(0)
                        .createdAt(LocalDateTime.now())
                        .build();

                answerRepository.save(answer);
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload answer: " + e.getMessage(), e);
        } finally {
            // Hapus temp files
            if (tempVideo != null && tempVideo.exists()) {
                tempVideo.delete();
            }
            if (tempAudio != null && tempAudio.exists()) {
                tempAudio.delete();
            }
        }
    }

    @Override
    public List<CandidateListProjection> getCandidateList() {
        return participantRepository.getCandidateList();
    }

    @Override
    public List<CandidateListProjection> getCandidateListByInterviewId(UUID interviewId) {
        return participantRepository.findCandidatesByInterviewId(interviewId);
    }

    @Override
    public CandidateSummaryResponse getCandidateSummary(UUID interviewId) {
        return participantRepository.getCandidateSummary(interviewId);
    }

    
    @Override
    public CandidateResultResponse getCandidateResult(UUID candidateId) {

        List<Answer> answers = answerRepository.findResultByCandidateId(candidateId);

        if (answers.isEmpty()) {
            throw new RuntimeException("Candidate result not found");
        }

        InterviewParticipant participant = answers.get(0).getParticipant();
        String name = participant.getCandidate().getUser().getName();

        List<QuestionAnswerResponse> questionAnswers = answers.stream().map(a -> {

            QuestionAnswerResponse dto = new QuestionAnswerResponse();

            dto.setQuestionId(a.getQuestion().getId());
            dto.setQuestionText(a.getQuestion().getQuestionText());
            dto.setAnswerTranscript(a.getTranscript());

            dto.setVideoUrl(
                    minioService.getPresignedUrl(a.getAudioPath())
            );

            dto.setFileName(a.getAudioPath());

            dto.setTechnicalFundamentalScore(a.getTechnicalFundamentalScore());
            dto.setProblemSolvingScore(a.getProblemSolvingScore());
            dto.setCommunicationScore(a.getCommunicationScore());
            dto.setBreakTime(a.getBreakTime());
            dto.setAnswerTime(a.getAnswerTime());
            return dto;

        }).toList();

        CandidateResultResponse result = new CandidateResultResponse();

        result.setName(name);
        result.setTotalScore(participant.getTotalScore());
        result.setRecommendation(participant.getRecommendation());
        result.setSummaryReason(participant.getSummaryReason());
        result.setAnswers(questionAnswers);

        return result;
    }
}
