package com.interview.ai_interview.controllers;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.interview.ai_interview.dto.response.SummaryResponse;
import com.interview.ai_interview.services.SummaryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/summary")
public class SummaryController {
    private final SummaryService summaryService;

    @GetMapping("/hiring/{interviewId}")
    public ResponseEntity<SummaryResponse> getInterviewSummary(@PathVariable UUID interviewId,
            @RequestParam("purpose") String params) {
        SummaryResponse response = summaryService.getSummary(interviewId, params);
        return ResponseEntity.ok(response);
    }
}
