package com.english.learning_service.mapper;


import com.english.learning_service.dto.request.PlanDetailRequest;
import com.english.learning_service.dto.request.PlanGroupRequest;
import com.english.learning_service.dto.request.PlanRequest;
import com.english.learning_service.dto.response.PlanDetailResponse;
import com.english.learning_service.dto.response.PlanGroupResponse;
import com.english.learning_service.dto.response.PlanResponse;
import com.english.learning_service.entity.Plan;
import com.english.learning_service.entity.PlanDetail;
import com.english.learning_service.entity.PlanGroup;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PlanMapper {
    Plan toPlan(PlanRequest request);

    PlanResponse toPlanResponse(PlanRequest planRequest);

    PlanGroup toPlanGroup(PlanGroupRequest request);
    List<PlanGroup> toPlanGroups(List<PlanGroupRequest> request);

    PlanDetail toPlanDetail(PlanDetailRequest request);
    List<PlanDetail> toPlanDetails(List<PlanDetailRequest> request);

    PlanResponse toPlanResponse(Plan plan);
    List<PlanResponse> toPlanResponses(List<Plan> plans);

    PlanGroupResponse toPlanGroupResponse(PlanGroup group);
    List<PlanGroupResponse> toPlanGroupResponses(List<PlanGroup> groups);

    PlanDetailResponse toPlanDetailResponse(PlanDetail detail);
    List<PlanDetailResponse> toPlanDetailResponses(List<PlanDetail> details);

}
