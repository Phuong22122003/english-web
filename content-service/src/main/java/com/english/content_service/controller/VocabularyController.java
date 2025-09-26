package com.english.content_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.english.content_service.service.VocabularyService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/vocabulary")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class VocabularyController {
    VocabularyService vocabularyService;

    @GetMapping("/topics")
    public ResponseEntity<?> getAllTopics(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok().body(vocabularyService.getTopics(page, size));
    }
    @GetMapping("/{topic_id}/vocabularies")
    public ResponseEntity<?> getVocabulariesByTopicId(@PathVariable(name = "topic_id") String topicId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok().body(vocabularyService.getVocabulariesByTopicId(topicId, page, size));
    }
    @GetMapping("/{topic_id}/tests")
    public ResponseEntity<?> getTestsByTopicId(@PathVariable(name = "topic_id") String topicId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok().body(vocabularyService.getTestsByTopicId(topicId, page, size));
    }
    @GetMapping("/tests/{test_id}/questions")
    public ResponseEntity<?> getTestQuestionsByTestId(@PathVariable(name = "test_id") String testId) {
        return ResponseEntity.ok().body(vocabularyService.getTestQuestionsByTestId(testId));
    }
}
