package com.english.learning_service.controller;

import com.english.learning_service.dto.request.PlanRequest;
import com.english.learning_service.dto.response.PlanResponse;
import com.english.learning_service.service.PlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/plan")
public class PlanController {

    private final PlanService planService;

    @Autowired
    public PlanController(PlanService planService) {
        this.planService = planService;
    }

    /**
     * Create a new plan
     * Example: POST /plan
     */
    @PostMapping
    public ResponseEntity<PlanResponse> addPlan(@RequestBody PlanRequest request) {
        PlanResponse response = planService.addPlan(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Get paginated list of plans
     * Example: GET /plan?page=0&size=10
     */
    @GetMapping
    public ResponseEntity<Page<PlanResponse>> getPlans(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<PlanResponse> response = planService.getPlan(page, size);
        return ResponseEntity.ok(response);
    }

    /**
     * Get plan detail by ID
     * Example: GET /plan/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<PlanResponse> getPlanDetail(@PathVariable("id") String planId) {
        PlanResponse response = planService.getPlanDetail(planId);
        return ResponseEntity.ok(response);
    }
}
