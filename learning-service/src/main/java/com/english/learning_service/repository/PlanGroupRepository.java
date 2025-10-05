package com.english.learning_service.repository;

import com.english.learning_service.entity.PlanGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanGroupRepository extends JpaRepository<PlanGroup, String> {
    List<PlanGroup> findByPlanId(String planId);
}
