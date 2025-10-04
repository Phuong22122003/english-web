package com.english.content_service.controller;

import com.english.content_service.dto.request.GrammarRequest;
import com.english.content_service.dto.request.GrammarTestRequest;
import com.english.content_service.dto.request.GrammarTopicRequest;
import com.english.content_service.dto.response.GrammarTopicResponse;
import com.english.content_service.service.GrammarService;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/grammar")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class GrammarController {

    GrammarService grammarService;

    //grammar topic
    @GetMapping("/topics")
    public ResponseEntity<?> getTopics(@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(grammarService.getTopics(page, size));
    }
    @PostMapping("/topics")
    public ResponseEntity<GrammarTopicResponse> addTopic(@RequestPart(name = "topic") GrammarTopicRequest request, @RequestPart("image")MultipartFile image){
        return ResponseEntity.ok().body(grammarService.addTopic(request,image));
    }

    //grammar
    @PostMapping("/{topic_id}/grammars")
    public ResponseEntity<?> addGrammar(@PathVariable(name = "topic_id") String topicId, @RequestBody GrammarRequest grammar){
        return ResponseEntity.ok().body(grammarService.addGrammar(topicId,grammar));
    }

    @GetMapping("/{grammar_id}")
    public ResponseEntity<?> getGrammarById(@PathVariable("grammar_id") String grammarId) {
        return ResponseEntity.ok(grammarService.getGrammarsByTopicId(grammarId));
    }

    //test
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

    @PostMapping("/tests/{topic_id}")
    public ResponseEntity<?> addTest(@PathVariable(name = "topic_id") String topicId, @RequestBody GrammarTestRequest testRequest){
        return ResponseEntity.ok().body(grammarService.addTest(topicId,testRequest));
    }
}
