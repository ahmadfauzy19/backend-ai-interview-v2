package com.interview.ai_interview.dto.response;

import java.util.UUID;

import com.interview.ai_interview.models.Interview;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PendingAnswerResponse {
    private UUID jobId;
    private UUID answerId;
    private UUID participantId;
    private UUID interviewId;
    private Interview interview;
    private String questionText;
    private String transcript;
}
