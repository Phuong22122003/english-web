package com.english.content_service.httpclient;

import com.english.content_service.dto.request.TopicRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "Topic",url = "${app.services.agent}")
public interface AgentClient {
    @PostMapping("/topics")
    public void addTopicTopVectorDB(TopicRequest request);
}
