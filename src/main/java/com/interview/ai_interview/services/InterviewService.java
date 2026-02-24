package com.interview.ai_interview.services;

import com.interview.ai_interview.dto.request.CreateInterviewRequest;
import com.interview.ai_interview.dto.response.InterviewDetailResponse;
import com.interview.ai_interview.dto.response.InterviewResponse;
import java.util.List;
import java.util.UUID;

public interface InterviewService {

    InterviewResponse createInterview(CreateInterviewRequest request);

    InterviewDetailResponse getById(UUID id);

    List<InterviewResponse> getAll();
}