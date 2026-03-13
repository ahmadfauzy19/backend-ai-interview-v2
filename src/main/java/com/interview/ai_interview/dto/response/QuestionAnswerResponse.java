package com.interview.ai_interview.dto.response;

import lombok.Data;

import java.util.UUID;

@Data
public class QuestionAnswerResponse {

    private UUID questionId;
    private String questionText;
    private String answerTranscript;
    private String videoUrl;
    private String fileName;
    private Float technicalFundamentalScore;
    private Float problemSolvingScore;
    private Float communicationScore;
    private String breakTime;
    private String answerTime;
}