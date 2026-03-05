package com.interview.ai_interview.repositories;

import com.interview.ai_interview.models.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface QuestionRepository extends JpaRepository<Question, UUID> {
    @Query("""
        SELECT COUNT(q)
        FROM Question q
        WHERE q.interview.id = :interviewId
    """)
    long countByInterviewId(UUID interviewId);
}