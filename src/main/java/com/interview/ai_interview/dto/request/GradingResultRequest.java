package com.interview.ai_interview.dto.request;

import java.util.List;

import lombok.Data;

@Data
public class GradingResultRequest {
    private String interviewId;
    private CandidateEvaluation candidateEvaluation;
    private List<QuestionResult> questionResults;
    private Integer totalScore;
    private String overallAssessment;
    private Recommendation recommendation;

    @Data
    public static class CandidateEvaluation {
        private String roleTarget;
        private String levelTarget;
        private String purpose;
    }

    @Data
    public static class QuestionResult {
        private String questionId;
        private String answerId;
        private Integer score;
        private String kategori;
        private String kelebihan;
        private String kekurangan;
        private String analisisTeknis;
    }

    @Data
    public static class Recommendation {
        private String decision;
        private String reason;
    }
}