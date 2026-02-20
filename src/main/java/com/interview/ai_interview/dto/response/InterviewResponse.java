package com.interview.ai_interview.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

import com.interview.ai_interview.models.InterviewMode;
import com.interview.ai_interview.models.InterviewStatus;
@Data
@Builder
public class InterviewResponse {

    private UUID id;
    private String name;
    private String description;
    private String objective;
    private InterviewMode mode;
    private InterviewStatus status;
    private UUID createdBy;
    private LocalDateTime createdAt;
}
