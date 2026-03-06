package com.interview.ai_interview.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class QuestionResponse {

    private UUID id;
    private String questionText;
    private Integer orderNumber;
}