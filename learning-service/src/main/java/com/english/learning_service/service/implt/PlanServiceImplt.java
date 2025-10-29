package com.english.learning_service.service.implt;

import com.english.dto.response.GrammarTopicResponse;
import com.english.dto.response.ListeningTopicResponse;
import com.english.dto.response.VocabTopicResponse;
import com.english.exception.AppException;
import com.english.exception.NotFoundException;
import com.english.learning_service.dto.request.*;
import com.english.learning_service.dto.response.PlanGroupResponse;
import com.english.learning_service.dto.response.PlanResponse;
import com.english.learning_service.entity.ExamHistory;
import com.english.learning_service.entity.Plan;
import com.english.learning_service.entity.PlanDetail;
import com.english.learning_service.entity.PlanGroup;
import com.english.learning_service.enums.ItemTypeEnum;
import com.english.learning_service.httpclient.AgentClient;
import com.english.learning_service.httpclient.GrammarClient;
import com.english.learning_service.httpclient.ListeningClient;
import com.english.learning_service.httpclient.VocabularyClient;
import com.english.learning_service.mapper.PlanMapper;
import com.english.learning_service.repository.ExamHistoryRepository;
import com.english.learning_service.repository.PlanDetailRepository;
import com.english.learning_service.repository.PlanGroupRepository;
import com.english.learning_service.repository.PlanRepository;
import com.english.learning_service.service.PlanService;
import com.english.learning_service.util.JwtUtils;
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
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


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
    ExamHistoryRepository examHistoryRepository;
    AgentClient agentClient;
    ListeningClient listeningClient;
    VocabularyClient vocabularyClient;
    GrammarClient grammarClient;

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
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
    public SseEmitter addPlanByAgent(PlanIntentRequest request) {
        String userId = JwtUtils.extractUserId(request.getJwt());

        UserInfoRequest userInfoRequest = new UserInfoRequest();
        userInfoRequest.setLevel(request.getLevel());
        userInfoRequest.setDescription(request.getDescription());
        userInfoRequest.setLevel(request.getLevel());
        userInfoRequest.setUserId(userId);

        List<ExamHistory> examHistories = this.examHistoryRepository.findTop5ByUserIdOrderByTakenAtDesc(userId);
        examHistories.forEach(e->{
            e.setId(null);
            e.setUserId(null);
            e.setTestId(null);
            e.setSubmittedAt(null);
        });
        userInfoRequest.setRecentExamHistory(examHistories);
        agentClient.generatePlan(userInfoRequest);

        SseEmitter emitter = new SseEmitter(0L); // Không timeout
        emitters.put(userId, emitter);

        emitter.onCompletion(() -> emitters.remove(userId));
        emitter.onTimeout(() -> emitters.remove(userId));
        emitter.onError((e) -> emitters.remove(userId));
        return emitter;
    }

    @Override
    public void sendNotification(CallBackRequest planRequest) {

        String userId = planRequest.getUserId();

        SseEmitter emitter = emitters.get(userId);
        if (emitter == null) return;

        // 🔹 Danh sách ID của các topic
        List<String> vocabIds = new ArrayList<>();
        List<String> grammarIds = new ArrayList<>();
        List<String> listeningIds = new ArrayList<>();

        // 🔹 Map để chứa topic đã lấy được
        Map<String, VocabTopicResponse> vocabMap = new HashMap<>();
        Map<String, GrammarTopicResponse> grammarMap = new HashMap<>();
        Map<String, ListeningTopicResponse> listeningMap = new HashMap<>();
        for(var group:planRequest.getPlanGroups()){
            group.setPlanDetails(group.getDetails());
        }
        // 🔹 Chuyển request sang response DTO (mapper bạn đã có)
        PlanResponse planResponse = planMapper.toPlanResponse(planRequest);

        // 👉 B1: Gom ID theo loại
        for (var group : planRequest.getPlanGroups()) {
            for (var detail : group.getDetails()) {
                switch (detail.getTopicType()) {
                    case VOCABULARY -> vocabIds.add(detail.getTopicId());
                    case GRAMMAR -> grammarIds.add(detail.getTopicId());
                    case LISTENING -> listeningIds.add(detail.getTopicId());
                }
            }
        }

        // 👉 B2: Gọi sang các service khác lấy dữ liệu
        if (!vocabIds.isEmpty()) {
            List<VocabTopicResponse> vocabTopics = vocabularyClient.getTopicsByIds(vocabIds);
            for (VocabTopicResponse v : vocabTopics) {
                vocabMap.put(v.getId(), v);
            }
        }

        if (!grammarIds.isEmpty()) {
            List<GrammarTopicResponse> grammarTopics = grammarClient.getTopicsByIds(grammarIds);
            for (GrammarTopicResponse g : grammarTopics) {
                grammarMap.put(g.getId(), g);
            }
        }

        if (!listeningIds.isEmpty()) {
            List<ListeningTopicResponse> listeningTopics = listeningClient.getTopicsByIds(listeningIds);
            for (ListeningTopicResponse l : listeningTopics) {
                listeningMap.put(l.getId(), l);
            }
        }

        // 👉 B3: Bổ sung thông tin topic cho từng detail
        for (var group : planResponse.getPlanGroups()) {
            for (var detail : group.getPlanDetails()) {
                switch (detail.getTopicType()) {
                    case VOCABULARY -> {
                        var topic = vocabMap.get(detail.getTopicId());
                        if (topic != null) {
                            detail.setTopicName(topic.getName());
                            detail.setDescription(topic.getDescription());
                        }
                    }
                    case GRAMMAR -> {
                        var topic = grammarMap.get(detail.getTopicId());
                        if (topic != null) {
                            detail.setTopicName(topic.getName());
                            detail.setDescription(topic.getDescription());
                        }
                    }
                    case LISTENING -> {
                        var topic = listeningMap.get(detail.getTopicId());
                        if (topic != null) {
                            detail.setTopicName(topic.getName());
                            detail.setDescription(topic.getDescription());
                        }
                    }
                }
            }
        }

        // 👉 B4: Gửi SSE event
        try {
            emitter.send(SseEmitter.event()
                    .name("UPDATE")
                    .data(planResponse)); // gửi bản đã enrich
        } catch (IOException e) {
            emitter.completeWithError(e);
        }
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
        List<String> vocabIds = new ArrayList<>();
        List<String> listeningIds = new ArrayList<>();
        List<String> grammarIds = new ArrayList<>();
        for(var group: groups){
            var groupResponse = planMapper.toPlanGroupResponse(group);
            var details = planDetailRepository.findByPlanGroupId(group.getId());
            for(var detail: details){
                if(detail.getTopicType().equals(ItemTypeEnum.VOCABULARY)){
                    vocabIds.add(detail.getTopicId());
                }
                else if(detail.getTopicType().equals(ItemTypeEnum.LISTENING)){
                    listeningIds.add(detail.getTopicId());
                }
                else{
                    grammarIds.add(detail.getTopicId());
                }
            }
            groupResponse.setPlanDetails(planMapper.toPlanDetailResponses(details));
            response.getPlanGroups().add(groupResponse);
        }
        Map<String, GrammarTopicResponse> grammarMap = new HashMap<>();
        Map<String, VocabTopicResponse> vocabMap = new HashMap<>();
        Map<String, ListeningTopicResponse> listeningMap = new HashMap<>();

        if (!grammarIds.isEmpty()) {
            List<GrammarTopicResponse> grammarTopics = grammarClient.getTopicsByIds(grammarIds);
            for (GrammarTopicResponse g : grammarTopics) {
                grammarMap.put(g.getId(), g);
            }
        }

        if (!vocabIds.isEmpty()) {
            List<VocabTopicResponse> vocabTopics = vocabularyClient.getTopicsByIds(vocabIds);
            for (VocabTopicResponse v : vocabTopics) {
                vocabMap.put(v.getId(), v);
            }
        }

        if (!listeningIds.isEmpty()) {
            List<ListeningTopicResponse> listeningTopics = listeningClient.getTopicsByIds(listeningIds);
            for (ListeningTopicResponse l : listeningTopics) {
                listeningMap.put(l.getId(), l);
            }
        }
        for(var group: response.getPlanGroups()){
            for(var detail: group.getPlanDetails()){
                switch (detail.getTopicType()) {

                    case VOCABULARY -> {
                        var topic = vocabMap.get(detail.getTopicId());
                        if (topic == null) continue;
                        detail.setTopicName(topic.getName());
                        detail.setDescription(topic.getDescription());
                    }

                    case GRAMMAR -> {
                        var topic = grammarMap.get(detail.getTopicId());
                        if (topic == null) continue;
                        detail.setTopicName(topic.getName());
                        detail.setDescription(topic.getDescription());
                    }

                    case LISTENING -> {
                        var topic = listeningMap.get(detail.getTopicId());
                        if (topic == null) continue;
                        detail.setTopicName(topic.getName());
                        detail.setDescription(topic.getDescription());
                    }
                }
            }
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
