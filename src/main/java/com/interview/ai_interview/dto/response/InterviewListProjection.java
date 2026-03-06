package com.interview.ai_interview.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public interface InterviewListProjection {

    UUID getId();
    String getName();
    String getContext();
    String getObjective();
    String getRoleTarget();
    String getLevelTarget();
    String getTechnology();
    String getPurpose();
    String getStatus();
    UUID getCreatedBy();
    LocalDateTime getCreatedAt();
    Boolean getIsAnswered();
}
