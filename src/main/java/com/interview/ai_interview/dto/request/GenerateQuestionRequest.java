package com.interview.ai_interview.dto.request;

import com.interview.ai_interview.models.InterviewMode;

import lombok.Data;

@Data
public class GenerateQuestionRequest {

    private String name;
    private String objective;
    private String context;
    private Number number;
    private String roleTarget;
    private String levelTarget;
    private String technology;
    private InterviewMode purpose;
}
