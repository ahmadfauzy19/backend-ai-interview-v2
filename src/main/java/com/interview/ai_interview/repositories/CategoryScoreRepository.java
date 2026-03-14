package com.interview.ai_interview.repositories;

import com.interview.ai_interview.models.CategoryScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryScoreRepository extends JpaRepository<CategoryScore, UUID> {    
    Optional<CategoryScore> findByCategory(String category);
}
