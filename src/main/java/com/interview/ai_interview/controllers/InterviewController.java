package com.interview.ai_interview.controllers;

import com.interview.ai_interview.dto.request.CreateInterviewRequest;
import com.interview.ai_interview.dto.response.InterviewResponse;
import com.interview.ai_interview.dto.response.InterviewDetailResponse;
import com.interview.ai_interview.services.InterviewService;
import com.interview.ai_interview.utils.CustomUserDetail;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/interviews")
@RequiredArgsConstructor
public class InterviewController {

    private final InterviewService interviewService;

    @PreAuthorize("hasRole('ADMIN') or hasRole('INTERVIEWER')")
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetail user = (CustomUserDetail) authentication.getPrincipal();

        UUID userId = user.getId();
        return interviewService.getAll(userId);
    }
}