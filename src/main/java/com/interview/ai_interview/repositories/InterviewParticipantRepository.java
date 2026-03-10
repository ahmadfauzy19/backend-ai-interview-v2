package com.interview.ai_interview.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.interview.ai_interview.dto.response.CandidateListProjection;
import com.interview.ai_interview.dto.response.CandidateSummaryResponse;
import com.interview.ai_interview.models.InterviewParticipant;

@Repository
public interface InterviewParticipantRepository
        extends JpaRepository<InterviewParticipant, UUID> {
    Optional<InterviewParticipant> findByInterviewIdAndCandidateId(UUID interviewId, UUID candidateId);
    Optional<InterviewParticipant> findByInterview_Id(UUID interviewId);
    @Query("""
    SELECT
        c.id as candidateId,
        u.name as name,
        p.startedAt as startedAt
    FROM InterviewParticipant p
    JOIN p.candidate c
    JOIN c.user u
    ORDER BY p.startedAt DESC
    """)
    List<CandidateListProjection> getCandidateList();

    @Query("""
    SELECT
        c.id as candidateId,
        u.name as name,
        p.startedAt as startedAt
    FROM InterviewParticipant p
    JOIN p.candidate c
    JOIN c.user u
    WHERE p.interview.id = :interviewId
    ORDER BY p.startedAt DESC
    """)
    List<CandidateListProjection> findCandidatesByInterviewId(UUID interviewId);

    @Query("""
    SELECT
        COUNT(p.id) as totalCandidate,
        SUM(CASE WHEN p.finishedAt IS NOT NULL THEN 1 ELSE 0 END) as totalCompleted,
        SUM(CASE WHEN p.finishedAt IS NULL THEN 1 ELSE 0 END) as totalPending,
        COALESCE(AVG(p.totalScore),0) as averageScore
    FROM InterviewParticipant p
    WHERE p.interview.id = :interviewId
    """)
    CandidateSummaryResponse getCandidateSummary(UUID interviewId);

    @Query("SELECT ip FROM InterviewParticipant ip WHERE NOT EXISTS " +
           "(SELECT gj FROM GradingJob gj WHERE gj.participant = ip)")
    List<InterviewParticipant> findParticipantsWithoutGradingJob();
}
