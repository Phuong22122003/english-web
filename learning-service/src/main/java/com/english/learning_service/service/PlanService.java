package com.english.learning_service.service;

import com.english.learning_service.dto.request.PlanRequest;
import com.english.learning_service.dto.response.PlanResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface PlanService {
    PlanResponse addPlan(PlanRequest request);
    Page<PlanResponse> getPlan(int page, int size);
    PlanResponse getPlanDetail(String planId);
    void deletePlan(String planId);
    PlanResponse editPlan(String planId, PlanRequest planRequest);
}
