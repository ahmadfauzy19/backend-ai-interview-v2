package com.interview.ai_interview.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GradingResponse {
    private String answerId;
    private InterviewInfo interview;
    private String question;
    private String answer;

    @Data
    @Builder
    public static class InterviewInfo {
        private String id;
        private String name;
        private String context;
        private String objective;
        private String roleTarget;
        private String levelTarget;
        private String technology;
        private String purpose;
    }
}
