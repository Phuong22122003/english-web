package com.english.learning_service.httpclient;

import com.english.dto.response.ListeningTestReponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "listening",url = "${app.services.content}/listening")
public interface ListeningClient {
    @GetMapping("/tests")
    public List<ListeningTestReponse> getTestsByIds(@RequestParam(name = "ids") List<String> ids);
    @GetMapping("/tests/{test_id}")
    public com.english.learning_service.dto.response.ListeningTestReponse getTestDetail(@PathVariable("test_id") String testId);
}
