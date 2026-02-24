package com.interview.ai_interview.repositories;

import com.interview.ai_interview.models.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface QuestionRepository extends JpaRepository<Question, UUID> {
}