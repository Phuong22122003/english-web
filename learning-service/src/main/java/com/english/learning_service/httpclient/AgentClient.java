package com.english.learning_service.httpclient;


import com.english.learning_service.dto.request.UserInfoRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "agent",url = "${app.services.agent}")
public interface AgentClient {
    @PostMapping("/agent/plan")
    public void generatePlan(UserInfoRequest userInfoRequest);
}
