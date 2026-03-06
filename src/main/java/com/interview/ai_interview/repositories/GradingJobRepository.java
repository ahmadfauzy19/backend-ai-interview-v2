package com.interview.ai_interview.repositories;

import com.interview.ai_interview.models.GradingJob;
import com.interview.ai_interview.models.GradingStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import jakarta.persistence.LockModeType;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;
import java.util.Optional;

@Repository
public interface GradingJobRepository extends JpaRepository<GradingJob, UUID> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT g FROM GradingJob g
        WHERE g.status = 'PENDING'
        ORDER BY g.createdAt ASC
    """)
    List<GradingJob> findPendingForProcessing(Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT g FROM GradingJob g WHERE g.id = :id")
    Optional<GradingJob> findByIdForUpdate(UUID id);

    boolean existsByParticipant_IdAndStatusIn(
            UUID participantId,
            List<GradingStatusEnum> statuses
    );
}