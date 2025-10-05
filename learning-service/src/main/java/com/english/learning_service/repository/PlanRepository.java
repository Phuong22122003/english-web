package com.english.learning_service.repository;

import com.english.learning_service.entity.Plan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanRepository extends JpaRepository<Plan, String> {
    Page<Plan> findPlanByUserId(String userId, Pageable pageable);
}
