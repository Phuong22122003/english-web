package com.english.learning_service.httpclient;

import com.english.dto.response.GetVocabularyTestQuestionResponse;
import com.english.dto.response.VocabularyTestResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "vocabulary",url = "${app.services.content}")
public interface VocabularyClient {
    @GetMapping("/vocabulary/tests")
    public List<VocabularyTestResponse> getTestsByIds(@RequestParam(name = "ids") List<String> ids);
    @GetMapping("/tests/{test_id}/questions")
    public GetVocabularyTestQuestionResponse getTestQuestionsByTestId(@PathVariable(name = "test_id") String testId);
}
