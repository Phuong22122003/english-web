package com.english.learning_service.repository;

import com.english.learning_service.entity.PlanDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanDetailRepository extends JpaRepository<PlanDetail, String> {
    public List<PlanDetail> findByPlanGroupId(String planGroupId);
}
