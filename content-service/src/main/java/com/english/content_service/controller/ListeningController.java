package com.english.content_service.controller;

import com.english.content_service.dto.request.ListeningRequest;
import com.english.content_service.dto.request.ListeningTestRequest;
import com.english.content_service.dto.request.ListeningTopicRequest;
import com.english.content_service.dto.response.ListeningResponse;
import com.english.content_service.dto.response.ListeningTestReponse;
import com.english.content_service.dto.response.ListeningTopicResponse;
import com.english.content_service.service.ListeningService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/listening")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class ListeningController {

    ListeningService listeningService;

    // ------------------- TOPIC -------------------

    @GetMapping("/topics")
    public ResponseEntity<?> getTopics(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(listeningService.getTopics(page, size));
    }

    @PostMapping("/topics")
    public ResponseEntity<ListeningTopicResponse> addTopic(@RequestPart("topic") ListeningTopicRequest request,
                                                           @RequestPart("image") MultipartFile imageFile) {
        return ResponseEntity.ok(listeningService.addTopic(request, imageFile));
    }

    // ------------------- LISTENING -------------------

    @GetMapping("/topics/{topic_id}/listenings")
    public ResponseEntity<ListeningTopicResponse> getListeningsByTopic(@PathVariable("topic_id") String topicId) {
        return ResponseEntity.ok(listeningService.getListeningByTopic(topicId));
    }

    @PostMapping("/topics/{topic_id}/listenings")
    public ResponseEntity<List<ListeningResponse>> addListeningList(
            @PathVariable("topic_id") String topicId,
            @RequestPart("requests") List<ListeningRequest> requests,
            @RequestPart(value = "images", required = false) List<MultipartFile> imageFiles,
            @RequestPart(value = "audios", required = false) List<MultipartFile> audioFiles) {

        return ResponseEntity.ok(listeningService.addListeningList(topicId, requests, imageFiles, audioFiles));
    }

    // ------------------- TEST -------------------

    // Lấy danh sách test theo topic
    @GetMapping("/topics/{topic_id}/tests")
    public ResponseEntity<ListeningTopicResponse> getTestsByTopic(@PathVariable("topic_id") String topicId,
                                                                  @RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(listeningService.getTestsByTopic(topicId, page, size));
    }

    // Thêm test mới (có thể kèm image/audio cho từng câu hỏi)
    @PostMapping("/topics/{topic_id}/tests")
    public ResponseEntity<ListeningTestReponse> addTest(
            @PathVariable("topic_id") String topicId,
            @RequestPart("request") ListeningTestRequest request,
            @RequestPart(value = "images", required = false) List<MultipartFile> imageFiles,
            @RequestPart(value = "audios", required = false) List<MultipartFile> audioFiles) {

        return ResponseEntity.ok(listeningService.addTest(topicId, request, imageFiles, audioFiles));
    }

    // Lấy chi tiết test
    @GetMapping("/tests/{test_id}")
    public ResponseEntity<ListeningTestReponse> getTestDetail(@PathVariable("test_id") String testId) {
        return ResponseEntity.ok(listeningService.getTestDetail(testId));
    }
}
