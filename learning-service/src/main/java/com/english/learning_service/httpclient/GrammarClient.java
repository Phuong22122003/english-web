package com.english.learning_service.httpclient;

import com.english.dto.response.GrammarTestResponse;
import com.english.learning_service.dto.response.GetGrammarTestQuestionsByTestIdResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "grammar",url = "${app.services.content}/grammar")
public interface GrammarClient {
    @GetMapping("/tests")
    public List<GrammarTestResponse> getTestsByIds(@RequestParam(name = "ids") List<String> ids);
    @GetMapping("/tests/{test_id}/questions")
    public GetGrammarTestQuestionsByTestIdResponse getTestQuestionsByTestId(@PathVariable("test_id") String testId);
}
