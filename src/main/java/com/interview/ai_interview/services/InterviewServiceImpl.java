package com.interview.ai_interview.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interview.ai_interview.dto.request.CreateInterviewRequest;
import com.interview.ai_interview.dto.request.GenerateQuestionRequest;
import com.interview.ai_interview.dto.response.GenerateQuestionAiResponse;
import com.interview.ai_interview.dto.response.InterviewResponse;
import com.interview.ai_interview.dto.response.QuestionResponse;
import com.interview.ai_interview.dto.response.InterviewDetailResponse;
import com.interview.ai_interview.models.Interview;
import com.interview.ai_interview.models.InterviewStatus;
import com.interview.ai_interview.models.Question;
import com.interview.ai_interview.repositories.InterviewRepository;
import com.interview.ai_interview.repositories.QuestionRepository;
import com.interview.ai_interview.utils.PromtingGenerateQuestion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InterviewServiceImpl implements InterviewService {

    private final InterviewRepository interviewRepository;
    private final QuestionRepository questionRepository;
    private final ObjectMapper objectMapper;
    private final PromtingGenerateQuestion promtingGenerateQuestion;
    private final GeminiClient geminiClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public InterviewResponse createInterview(CreateInterviewRequest request) {

        
        Interview interview = Interview.builder()
                .name(request.getName())
                .context(request.getContext())
                .objective(request.getObjective())
                .roleTarget(request.getRoleTarget())
                .levelTarget(request.getLevelTarget())
                .technology(request.getTechnology())
                .purpose(request.getPurpose())
                .status(InterviewStatus.DRAFT)
                .createdBy(request.getCreatedBy())
                .createdAt(LocalDateTime.now())
                .build();

        Interview savedInterview = interviewRepository.save(interview);

        // Generate AI Request
        GenerateQuestionRequest generateRequest =
                fromInterview(savedInterview, request.getNumber());

        String prompt = promtingGenerateQuestion.buildPrompt(generateRequest);

        String aiRawResponse = geminiClient.evaluate(
                promtingGenerateQuestion.getSystemPrompt(),
                prompt
        );

        if (aiRawResponse == null || aiRawResponse.isBlank()) {
            throw new RuntimeException("AI response empty");
        }

        try {
            GenerateQuestionAiResponse aiResponse =
                    objectMapper.readValue(aiRawResponse, GenerateQuestionAiResponse.class);

            if (aiResponse.getQuestions() == null || aiResponse.getQuestions().isEmpty()) {
                throw new RuntimeException("AI generated no questions");
            }

            // Save Questions (batch save)
            List<Question> questionList = new ArrayList<>();

            int order = 1;
            for (GenerateQuestionAiResponse.QuestionItem item : aiResponse.getQuestions()) {

                Question question = Question.builder()
                        .interview(savedInterview) // IMPORTANT: gunakan relasi
                        .questionText(item.getQuestion())
                        .orderNumber(order++)
                        .build();

                questionList.add(question);
            }

            questionRepository.saveAll(questionList);

        } catch (Exception e) {
            throw new RuntimeException("Failed parsing AI response: " + e.getMessage());
        }

        return mapToResponse(savedInterview);
    }

    @Override
    public InterviewDetailResponse getById(UUID id) {
        Interview interview = interviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Interview not found"));

        return mapToDetailResponse(interview);
    }

    @Override
    public List<InterviewResponse> getAll() {
        return interviewRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public static GenerateQuestionRequest fromInterview(Interview interview, Number number) {
        GenerateQuestionRequest req = new GenerateQuestionRequest();
        req.setName(interview.getName());
        req.setObjective(interview.getObjective());
        req.setContext(interview.getContext());
        req.setRoleTarget(interview.getRoleTarget());
        req.setLevelTarget(interview.getLevelTarget());
        req.setTechnology(interview.getTechnology());
        req.setPurpose(interview.getPurpose());
        req.setNumber(number);
        return req;
    }

    private InterviewResponse mapToResponse(Interview interview) {
        return InterviewResponse.builder()
                .id(interview.getId())
                .name(interview.getName())
                .context(interview.getContext())
                .objective(interview.getObjective())
                .purpose(interview.getPurpose())
                .roleTarget(interview.getRoleTarget())
                .levelTarget(interview.getLevelTarget())
                .technology(interview.getTechnology())
                .status(interview.getStatus())
                .createdBy(interview.getCreatedBy())
                .createdAt(interview.getCreatedAt())
                .build();
    }

    private InterviewDetailResponse mapToDetailResponse(Interview interview) {
        List<QuestionResponse> questionResponses =
            interview.getQuestions()
                    .stream()
                    .sorted((q1, q2) -> q1.getOrderNumber().compareTo(q2.getOrderNumber()))
                    .map(q -> QuestionResponse.builder()
                            .id(q.getId())
                            .questionText(q.getQuestionText())
                            .orderNumber(q.getOrderNumber())
                            .build())
                    .toList();

        return InterviewDetailResponse.builder()
                .id(interview.getId())
                .name(interview.getName())
                .context(interview.getContext())
                .objective(interview.getObjective())
                .purpose(interview.getPurpose())
                .roleTarget(interview.getRoleTarget())
                .levelTarget(interview.getLevelTarget())
                .technology(interview.getTechnology())
                .status(interview.getStatus())
                .createdBy(interview.getCreatedBy())
                .createdAt(interview.getCreatedAt())
                .questions(questionResponses)
                .build();
    }
}