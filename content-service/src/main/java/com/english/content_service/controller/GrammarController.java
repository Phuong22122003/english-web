package com.english.content_service.controller;

import com.english.content_service.service.GrammarService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
@RestController
@RequestMapping("/grammar")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class GrammarController {

    GrammarService grammarService;

    @GetMapping
    public ResponseEntity<?> getAllGrammars(@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(grammarService.getTopics(page, size));
    }

    @GetMapping("/{grammar_id}")
    public ResponseEntity<?> getGrammarById(@PathVariable("grammar_id") String grammarId) {
        return ResponseEntity.ok(grammarService.getGrammarsByTopicId(grammarId));
    }

    @GetMapping("/{grammar_id}/tests")
    public ResponseEntity<?> getTestsByGrammarId(@PathVariable("grammar_id") String grammarId,
                                                 @RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(grammarService.getTestsByGrammarId(grammarId, page, size));
    }

    @GetMapping("/tests/{test_id}/questions")
    public ResponseEntity<?> getTestQuestionsByTestId(@PathVariable("test_id") String testId) {
        return ResponseEntity.ok(grammarService.getTestQuestionsByTestId(testId));
    }
}
