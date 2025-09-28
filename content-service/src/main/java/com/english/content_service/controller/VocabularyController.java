package com.english.content_service.controller;

import com.english.content_service.dto.request.VocabularyRequest;
import com.english.content_service.dto.request.VocabularyTestRequest;
import com.english.dto.AppResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.english.content_service.dto.request.VocabTopicRequest;
import com.english.content_service.dto.response.VocabTopicResponse;
import com.english.content_service.service.VocabularyService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@RestController
@RequestMapping("/vocabulary")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class VocabularyController {
    VocabularyService vocabularyService;

    //topics
    @GetMapping("/topics")
    public ResponseEntity<?> getAllTopics(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok().body(vocabularyService.getTopics(page, size));
    }
    @PostMapping("/topics")
    public ResponseEntity<VocabTopicResponse> createTopic(@RequestPart VocabTopicRequest topic, @RequestPart(required = false,name = "image") MultipartFile imageFile) {
        return ResponseEntity.ok().body(vocabularyService.addTopic(topic, imageFile));
    }
    @DeleteMapping("/topics/{topic_id}")
    public ResponseEntity<?> deleteTopic(@PathVariable(name = "topic_id") String topicId){
        this.vocabularyService.deleteTopic(topicId);
        return ResponseEntity.ok().body(AppResponse.builder().message("Delete topic successfully").build());
    }
    // vocabulary
    @GetMapping("/{topic_id}/vocabularies")
    public ResponseEntity<?> getVocabulariesByTopicId(@PathVariable(name = "topic_id") String topicId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok().body(vocabularyService.getVocabulariesByTopicId(topicId));
    }
    @PostMapping("/{topic_id}/vocabularies")
    public ResponseEntity<?> addVocabularies(@PathVariable("topic_id") String topicId, @RequestPart(name = "vocabularies") List<VocabularyRequest> requests,
                                             @RequestPart (name = "images") List<MultipartFile> imageFiles, @RequestPart(name = "audios") List<MultipartFile> audioFiles){
        return ResponseEntity.ok().body(vocabularyService.addVocabularies(topicId,requests,imageFiles,audioFiles));
    }
    //test
    @GetMapping("/{topic_id}/tests")
    public ResponseEntity<?> getTestsByTopicId(@PathVariable(name = "topic_id") String topicId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok().body(vocabularyService.getTestsByTopicId(topicId, page, size));
    }

    @PostMapping("/{topic_id}/tests")
    public  ResponseEntity<?> createTest(@PathVariable(name = "topic_id") String topicId,@RequestPart(name = "test")VocabularyTestRequest vocabularyTestRequest,@RequestPart(name = "images") List<MultipartFile> images){
        return ResponseEntity.ok().body(vocabularyService.addTest(topicId,vocabularyTestRequest,images));
    }

    @GetMapping("/tests/{test_id}/questions")
    public ResponseEntity<?> getTestQuestionsByTestId(@PathVariable(name = "test_id") String testId) {
        return ResponseEntity.ok().body(vocabularyService.getTestQuestionsByTestId(testId));
    }

}
