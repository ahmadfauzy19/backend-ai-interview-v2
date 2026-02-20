package com.interview.ai_interview.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.interview.ai_interview.models.Interview;

import java.util.UUID;

public interface InterviewRepository extends JpaRepository<Interview, UUID> {
}
