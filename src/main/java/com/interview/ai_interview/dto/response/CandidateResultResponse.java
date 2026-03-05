package com.interview.ai_interview.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class CandidateResultResponse {

    private String name;
    private Float totalScore;
    private String finalRecommendation;
    private List<QuestionAnswerResponse> answers;

}