package com.interview.ai_interview.services;

import org.springframework.web.multipart.MultipartFile;

import com.interview.ai_interview.dto.response.CandidateListProjection;
import com.interview.ai_interview.dto.response.CandidateResultResponse;
import com.interview.ai_interview.dto.response.CandidateSummaryResponse;

import java.util.List;
import java.util.UUID;

public interface AnswerService {
    void uploadAnswer(MultipartFile video, UUID questionId, UUID userId);
    List<CandidateListProjection> getCandidateList();
    CandidateResultResponse getCandidateResult(UUID candidateId);
    CandidateSummaryResponse getCandidateSummary(UUID interviewId);
    List<CandidateListProjection> getCandidateListByInterviewId(UUID interviewId);
}
