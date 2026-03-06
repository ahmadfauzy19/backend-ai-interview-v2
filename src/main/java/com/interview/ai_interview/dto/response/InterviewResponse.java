package com.interview.ai_interview.dto.response;

// import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

import com.interview.ai_interview.models.InterviewMode;
import com.interview.ai_interview.models.InterviewStatus;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
// @Builder
public class InterviewResponse {

    private UUID id;
    private String name;
    private String context;
    private String objective;
    private String roleTarget;
    private String levelTarget;
    private String technology;
    private InterviewMode purpose;
    private InterviewStatus status;
    private UUID createdBy;
    private LocalDateTime createdAt;
    private Boolean isAnswered;
}
