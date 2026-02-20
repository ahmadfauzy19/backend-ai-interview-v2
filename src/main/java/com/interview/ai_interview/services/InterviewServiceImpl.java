package com.interview.ai_interview.services;

import com.interview.ai_interview.dto.request.CreateInterviewRequest;
import com.interview.ai_interview.dto.response.InterviewResponse;

import com.interview.ai_interview.models.Interview;
import com.interview.ai_interview.models.InterviewStatus;
import com.interview.ai_interview.repositories.InterviewRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InterviewServiceImpl implements InterviewService {

    private final InterviewRepository interviewRepository;

    @Override
    public InterviewResponse createInterview(CreateInterviewRequest request) {

        // if (!userRole.equals("INTERVIEWER")) {
        //     throw new RuntimeException("Only INTERVIEWER can create interview");
        // }

        Interview interview = Interview.builder()
                .name(request.getName())
                .description(request.getDescription())
                .objective(request.getObjective())
                .mode(request.getMode())
                .status(InterviewStatus.DRAFT)
                .createdBy(request.getCreatedBy())
                .createdAt(LocalDateTime.now())
                .build();

        Interview saved = interviewRepository.save(interview);

        return mapToResponse(saved);
    }

    @Override
    public InterviewResponse getById(UUID id) {
        Interview interview = interviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Interview not found"));

        return mapToResponse(interview);
    }

    @Override
    public List<InterviewResponse> getAll() {
        return interviewRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private InterviewResponse mapToResponse(Interview interview) {
        return InterviewResponse.builder()
                .id(interview.getId())
                .name(interview.getName())
                .description(interview.getDescription())
                .objective(interview.getObjective())
                .mode(interview.getMode())
                .status(interview.getStatus())
                .createdBy(interview.getCreatedBy())
                .createdAt(interview.getCreatedAt())
                .build();
    }
}

