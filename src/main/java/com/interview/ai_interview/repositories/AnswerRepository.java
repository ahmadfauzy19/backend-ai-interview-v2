package com.interview.ai_interview.repositories;

import com.interview.ai_interview.dto.response.PendingAnswerResponse;
import com.interview.ai_interview.models.Answer;
import com.interview.ai_interview.models.TranscriptStatusEnum;

import jakarta.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.UUID;
import java.util.List;
import org.springframework.data.domain.Pageable;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, UUID> {
    @Query("""
        SELECT a FROM Answer a
        WHERE a.status = 'PENDING'
        ORDER BY a.createdAt ASC
    """)
    List<Answer> findPendingForProcessing(Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM Answer a WHERE a.id = :id")
    java.util.Optional<Answer> findByIdForUpdate(UUID id);

    @Query("""
        SELECT COUNT(a)
        FROM Answer a
        WHERE a.participant.id = :participantId
    """)
    long countAnswered(UUID participantId);
    
    @Query("""
        SELECT a
        FROM Answer a
        JOIN FETCH a.question q
        JOIN FETCH a.participant p
        JOIN FETCH p.candidate c
        JOIN FETCH c.user
        WHERE c.id = :candidateId
    """)
    List<Answer> findResultByCandidateId(UUID candidateId);

    long countByParticipantIdAndStatus(UUID participantId, TranscriptStatusEnum status);

    @Query("""
        SELECT new com.interview.ai_interview.dto.response.PendingAnswerResponse(
            gj.id,
            a.id,
            ip.id,
            ip.interview.id,
            ip.interview,
            q.questionText,
            a.transcript
        )
        FROM GradingJob gj
        JOIN gj.participant ip
        JOIN Answer a ON a.participant.id = ip.id
        JOIN Question q ON a.question.id = q.id
        WHERE gj.status = com.interview.ai_interview.models.GradingStatusEnum.PENDING
          AND a.status = com.interview.ai_interview.models.TranscriptStatusEnum.DONE
    """)
    List<PendingAnswerResponse> findAllAnswersForPendingJobs();
}
