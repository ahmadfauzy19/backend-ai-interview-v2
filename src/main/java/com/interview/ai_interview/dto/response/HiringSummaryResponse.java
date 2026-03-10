package com.interview.ai_interview.dto.response;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HiringSummaryResponse implements SummaryResponse {
    private Integer strongHire;
    private Integer hire;
    private Integer consider;
    private Integer reject;
}

