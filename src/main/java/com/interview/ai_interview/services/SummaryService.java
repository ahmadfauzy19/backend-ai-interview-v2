package com.interview.ai_interview.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.interview.ai_interview.dto.response.HiringSummaryResponse;
import com.interview.ai_interview.dto.response.InternalAssessmentSummaryResponse;
import com.interview.ai_interview.dto.response.SummaryResponse;
import com.interview.ai_interview.models.Interview;
import com.interview.ai_interview.models.InterviewParticipant;
import com.interview.ai_interview.repositories.InterviewParticipantRepository;
import com.interview.ai_interview.repositories.InterviewRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SummaryService {
    private final InterviewRepository interviewRepository;
    private final InterviewParticipantRepository participantRepository;

    public SummaryResponse getSummary(UUID interviewId, String purpose) {
        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new EntityNotFoundException("Interview not found"));

        List<InterviewParticipant> participants = participantRepository.findByInterviewId(interviewId);

        if (purpose.equals("HIRING")) {
            return buildHiringSummary(participants);
        } else if (purpose.equals("INTERNAL_ASSESSMENT")) {
            return buildInternalSummary(participants);
        }

        throw new IllegalArgumentException("Unknown interview purpose: " + interview.getPurpose());
    }

    private HiringSummaryResponse buildHiringSummary(List<InterviewParticipant> participants) {
        long strongHire = 0, hire = 0, consider = 0, reject = 0;

        for (InterviewParticipant p : participants) {
            double score = p.getTotalScore();
            if (score >= 85) strongHire++;
            else if (score >= 75) hire++;
            else if (score >= 65) consider++;
            else reject++;
        }
        return HiringSummaryResponse.builder()
                .strongHire((int) strongHire)
                .hire((int) hire)
                .consider((int) consider)
                .reject((int) reject)
                .build();
    }

    private InternalAssessmentSummaryResponse buildInternalSummary(List<InterviewParticipant> participants) {
        long ready = 0, meets = 0, needsImp = 0, sigImp = 0;

        for (InterviewParticipant p : participants) {
            double score = p.getTotalScore();
            if (score >= 85) ready++;
            else if (score >= 70) meets++;
            else if (score >= 55) needsImp++;
            else sigImp++;
        }

        return InternalAssessmentSummaryResponse.builder()
                .readyForPromotion((int) ready)
                .meetsCurrentLevel((int) meets)
                .needsImprovement((int) needsImp)
                .significantImprovementRequired((int) sigImp)
                .build();
    }
}
