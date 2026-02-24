package com.interview.ai_interview.controllers;

import com.interview.ai_interview.dto.request.CreateInterviewRequest;
import com.interview.ai_interview.dto.response.InterviewResponse;
import com.interview.ai_interview.dto.response.InterviewDetailResponse;
import com.interview.ai_interview.services.InterviewService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/interviews")
@RequiredArgsConstructor
public class InterviewController {

    private final InterviewService interviewService;

    @PostMapping
    public InterviewResponse create(
            @Valid @RequestBody CreateInterviewRequest request
    ) {
        return interviewService.createInterview(request);
    }

    @GetMapping("/{id}")
    public InterviewDetailResponse getById(@PathVariable UUID id) {
        return interviewService.getById(id);
    }

    @GetMapping
    public List<InterviewResponse> getAll() {
        return interviewService.getAll();
    }
}