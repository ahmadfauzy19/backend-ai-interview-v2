package com.interview.ai_interview.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InternalAssessmentSummaryResponse implements SummaryResponse {
    private Integer readyForPromotion;
    private Integer meetsCurrentLevel;
    private Integer needsImprovement;
    private Integer significantImprovementRequired;
}
