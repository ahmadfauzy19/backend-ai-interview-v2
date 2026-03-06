package com.interview.ai_interview.repositories;

import com.interview.ai_interview.dto.response.CandidateListProjection;
import com.interview.ai_interview.models.InterviewParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;
import java.util.List;

@Repository
public interface InterviewParticipantRepository
        extends JpaRepository<InterviewParticipant, UUID> {
    Optional<InterviewParticipant> findByInterviewIdAndCandidateId(UUID interviewId, UUID candidateId);
    Optional<InterviewParticipant> findByInterview_Id(UUID interviewId);
    @Query("""
    SELECT
        c.id as candidateId,
        u.name as name,
        p.startedAt as startedAt,
        COUNT(c.id) as totalCandidate
    FROM InterviewParticipant p
    JOIN p.candidate c
    JOIN c.user u
    GROUP BY c.id, u.name, p.startedAt
    ORDER BY p.startedAt DESC
    """)
    List<CandidateListProjection> getCandidateList();

    @Query("""
    SELECT
        c.id as candidateId,
        u.name as name,
        p.startedAt as startedAt
        COUNT(c.id) as totalCandidate
    FROM InterviewParticipant p
    JOIN p.candidate c
    JOIN c.user u
    WHERE p.interview.id = :interviewId
    ORDER BY p.startedAt DESC
    """)
    List<CandidateListProjection> findCandidatesByInterviewId(UUID interviewId);
}
