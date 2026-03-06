package com.interview.ai_interview.repositories;

import com.interview.ai_interview.models.Answer;

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

    long countByParticipantIdAndStatusNot(UUID participantId, String status);

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
}
