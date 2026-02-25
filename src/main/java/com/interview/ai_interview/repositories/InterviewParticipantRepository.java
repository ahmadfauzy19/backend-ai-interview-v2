package com.interview.ai_interview.repositories;

import com.interview.ai_interview.models.InterviewParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InterviewParticipantRepository
        extends JpaRepository<InterviewParticipant, UUID> {

    Optional<InterviewParticipant> findByInterview_Id(UUID interviewId);
}
