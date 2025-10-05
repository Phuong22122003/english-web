package com.english.learning_service.service.implt;

import com.english.exception.NotFoundException;
import com.english.learning_service.dto.request.PlanRequest;
import com.english.learning_service.dto.response.PlanResponse;
import com.english.learning_service.entity.Plan;
import com.english.learning_service.entity.PlanGroup;
import com.english.learning_service.mapper.PlanMapper;
import com.english.learning_service.repository.PlanDetailRepository;
import com.english.learning_service.repository.PlanGroupRepository;
import com.english.learning_service.repository.PlanRepository;
import com.english.learning_service.service.PlanService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@Service
@Data
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PlanServiceImplt implements PlanService {
    PlanMapper planMapper;
    PlanRepository planRepository;
    PlanDetailRepository planDetailRepository;
    PlanGroupRepository planGroupRepository;
    @Override
    @Transactional
    public PlanResponse addPlan(PlanRequest request) {
        var context = SecurityContextHolder.getContext();
        String userId = context.getAuthentication().getName();
        List<PlanGroup> planGroups = new ArrayList<>();
        PlanResponse response;
        Plan plan = planMapper.toPlan(request);
        plan.setCompleted(false);
        plan.setUserId(userId);
        plan = planRepository.save(plan);

        response=planMapper.toPlanResponse(plan);
        response.setPlanGroups(new ArrayList<>());
        for(var g: request.getPlanGroups()){
            var group = planMapper.toPlanGroup(g);

            group.setPlan(plan);
            group = planGroupRepository.save(group);

            var detailRequests = g.getDetails();
            var details = planMapper.toPlanDetails(detailRequests);
            for(var d: details){d.setPlanGroup(group);}
            planDetailRepository.saveAll(details);

            var groupResponse = planMapper.toPlanGroupResponse(group);
            groupResponse.setPlanDetails(planMapper.toPlanDetailResponses(details));
            response.getPlanGroups().add(groupResponse);
        }

        return response;
    }

    @Override
    public Page<PlanResponse> getPlan(int page, int size) {
        var context = SecurityContextHolder.getContext();
        String userId = context.getAuthentication().getName();
        Page<Plan> plans = planRepository.findPlanByUserId(userId, PageRequest.of(page,size));
        List<PlanResponse> planResponses = planMapper.toPlanResponses(plans.getContent());
        return new PageImpl<>(planResponses,plans.getPageable(),plans.getTotalElements());
    }

    @Override
    public PlanResponse getPlanDetail(String planId) {
        Plan plan = planRepository.findById(planId).orElseThrow(()->new NotFoundException("Plan not found"));
        PlanResponse response = planMapper.toPlanResponse(plan);
        response.setPlanGroups(new ArrayList<>());
        List<PlanGroup> groups = planGroupRepository.findByPlanId(planId);
        for(var group: groups){
            var groupResponse = planMapper.toPlanGroupResponse(group);
            var details = planDetailRepository.findByPlanGroupId(group.getId());
            groupResponse.setPlanDetails(planMapper.toPlanDetailResponses(details));
            response.getPlanGroups().add(groupResponse);
        }
        return response;
    }

}
