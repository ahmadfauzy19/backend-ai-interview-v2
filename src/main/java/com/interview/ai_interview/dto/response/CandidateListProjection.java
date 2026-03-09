package com.interview.ai_interview.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public interface CandidateListProjection {

    UUID getCandidateId();
    String getName();
    Float getTotalScore();
    String getFinalRecommendation();
    LocalDateTime getStartedAt();
}