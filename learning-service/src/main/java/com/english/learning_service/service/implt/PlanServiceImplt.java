package com.english.learning_service.service.implt;

import com.english.exception.AppException;
import com.english.exception.NotFoundException;
import com.english.learning_service.dto.request.PlanGroupRequest;
import com.english.learning_service.dto.request.PlanRequest;
import com.english.learning_service.dto.response.PlanGroupResponse;
import com.english.learning_service.dto.response.PlanResponse;
import com.english.learning_service.entity.Plan;
import com.english.learning_service.entity.PlanDetail;
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

import java.util.*;


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

    @Transactional
    @Override
    public void deletePlan(String planId) {
        // 1. Tìm plan theo ID
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> AppException.builder()
                        .code(404)
                        .message("No plan was found")
                        .build());

        // 2. Lấy danh sách PlanGroup theo planId
        List<PlanGroup> planGroups = planGroupRepository.findByPlanId(planId);

        // 3. Xóa tất cả PlanDetail thuộc các PlanGroup này
        for (PlanGroup pg : planGroups) {
            List<PlanDetail> planDetails = planDetailRepository.findByPlanGroupId(pg.getId());
            if (!planDetails.isEmpty()) {
                planDetailRepository.deleteAll(planDetails);
            }
        }

        // 4. Xóa các PlanGroup liên quan
        if (!planGroups.isEmpty()) {
            planGroupRepository.deleteAll(planGroups);
        }

        // 5. Cuối cùng xóa Plan
        planRepository.delete(plan);
    }

    @Override
    @Transactional
    public PlanResponse editPlan(String planId, PlanRequest request) {
        // 1️⃣ Lấy plan hiện tại
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> AppException.builder()
                        .code(404)
                        .message("No plan was found")
                        .build());

        // 2️⃣ Cập nhật thông tin cơ bản
        plan.setTitle(request.getTitle());
        plan.setDescription(request.getDescription());
        plan.setStartDate(request.getStartDate());
        plan.setEndDate(request.getEndDate());
        plan.setTarget(request.getTarget());
        planRepository.save(plan);

        // 3️⃣ Xóa toàn bộ nhóm và chi tiết cũ (để dễ xử lý)
        List<PlanGroup> oldGroups = planGroupRepository.findByPlanId(planId);
        for (PlanGroup oldGroup : oldGroups) {
            List<PlanDetail> oldDetails = planDetailRepository.findByPlanGroupId(oldGroup.getId());
            if (!oldDetails.isEmpty()) {
                planDetailRepository.deleteAll(oldDetails);
            }
        }
        if (!oldGroups.isEmpty()) {
            planGroupRepository.deleteAll(oldGroups);
        }

        // 4️⃣ Thêm lại các nhóm và chi tiết mới theo request
        List<PlanGroupResponse> groupResponses = new ArrayList<>();
        for (PlanGroupRequest g : request.getPlanGroups()) {
            PlanGroup group = planMapper.toPlanGroup(g);
            group.setPlan(plan);
            group = planGroupRepository.save(group);

            List<PlanDetail> details = planMapper.toPlanDetails(g.getDetails());
            for (PlanDetail d : details) {
                d.setPlanGroup(group);
            }
            planDetailRepository.saveAll(details);

            PlanGroupResponse groupResponse = planMapper.toPlanGroupResponse(group);
            groupResponse.setPlanDetails(planMapper.toPlanDetailResponses(details));
            groupResponses.add(groupResponse);
        }

        // 5️⃣ Trả về phản hồi sau khi cập nhật
        PlanResponse response = planMapper.toPlanResponse(plan);
        response.setPlanGroups(groupResponses);
        return response;
    }

}
