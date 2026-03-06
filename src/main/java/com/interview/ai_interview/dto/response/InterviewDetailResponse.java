package com.interview.ai_interview.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class InterviewDetailResponse extends InterviewResponse {

    private List<QuestionResponse> questions;
}