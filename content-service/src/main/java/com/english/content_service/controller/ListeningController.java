package com.english.content_service.controller;

import com.english.content_service.dto.request.ListeningRequest;
import com.english.content_service.dto.request.ListeningTopicRequest;
import com.english.content_service.dto.response.ListeningResponse;
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

    // Lấy danh sách topic
    @GetMapping("/topics")
    public ResponseEntity<?> getTopics(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(listeningService.getTopics(page, size));
    }

    // Thêm topic mới
    @PostMapping("/topics")
    public ResponseEntity<ListeningTopicResponse> addTopic(@RequestPart("topic") ListeningTopicRequest request,
                                                           @RequestPart("image") MultipartFile imageFile) {
        return ResponseEntity.ok(listeningService.addTopic(request, imageFile));
    }

    // Lấy danh sách listening theo topicId
    @GetMapping("/topics/{topic_id}/listenings")
    public ResponseEntity<List<ListeningResponse>> getListeningsByTopic(@PathVariable("topic_id") String topicId) {
        return ResponseEntity.ok(listeningService.getListeningByTopic(topicId));
    }

    // Thêm danh sách listening (nhiều câu nghe trong 1 topic)
    @PostMapping("/topics/{topic_id}/listenings")
    public ResponseEntity<List<ListeningResponse>> addListeningList(
            @PathVariable("topic_id") String topicId,
            @RequestPart("requests") List<ListeningRequest> requests,
            @RequestPart("images") List<MultipartFile> imageFiles,
            @RequestPart("audios") List<MultipartFile> audioFiles) {

        return ResponseEntity.ok(listeningService.addListeningList(topicId, requests, imageFiles, audioFiles));
    }
}
