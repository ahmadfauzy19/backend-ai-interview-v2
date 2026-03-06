package com.interview.ai_interview.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.interview.ai_interview.dto.response.InterviewListProjection;
import com.interview.ai_interview.models.Interview;
import java.util.List;

public interface InterviewRepository extends JpaRepository<Interview, UUID> {
    @Query(value = """
    SELECT
        i.id,
        i.name,
        i.context,
        i.objective,
        i.role_target as roleTarget,
        i.level_target as levelTarget,
        i.technology,
        i.purpose,
        i.status,
        i.created_by as createdBy,
        i.created_at as createdAt,

        CASE 
            WHEN COUNT(q.id) = COUNT(a.id) AND COUNT(q.id) > 0
            THEN true
            ELSE false
        END as isAnswered

    FROM interviews i

    LEFT JOIN questions q
        ON q.interview_id = i.id

    LEFT JOIN interview_participants ip
        ON ip.interview_id = i.id
        AND ip.candidate_id = :candidateId

    LEFT JOIN answers a
        ON a.participant_id = ip.id
        AND a.question_id = q.id

    GROUP BY
        i.id

    ORDER BY i.created_at DESC
    """, nativeQuery = true)
    List<InterviewListProjection> findAllWithAnswerStatus(UUID candidateId);
    @Query("""
    SELECT
        i.id as id,
        i.name as name,
        i.context as context,
        i.objective as objective,
        i.roleTarget as roleTarget,
        i.levelTarget as levelTarget,
        i.technology as technology,
        i.purpose as purpose,
        i.status as status,
        i.createdBy as createdBy,
        i.createdAt as createdAt,
        false as isAnswered
    FROM Interview i
    """)
    List<InterviewListProjection> findAllWithoutCandidate();
}
