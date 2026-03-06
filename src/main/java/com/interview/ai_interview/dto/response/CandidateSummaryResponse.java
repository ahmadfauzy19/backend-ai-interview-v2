package com.interview.ai_interview.dto.response;

public interface CandidateSummaryResponse {
    Integer getTotalCandidate();
    Integer getTotalCompleted();
    Integer getTotalPending();
    Float getAverageScore();
}
