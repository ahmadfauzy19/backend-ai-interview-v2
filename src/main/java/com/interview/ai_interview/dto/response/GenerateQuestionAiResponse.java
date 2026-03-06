package com.interview.ai_interview.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class GenerateQuestionAiResponse {

    private String description;
    private List<QuestionItem> questions;

    @Data
    public static class QuestionItem {
        private String question;
    }
}