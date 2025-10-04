package com.english.content_service.service;

import com.english.content_service.dto.request.GrammarRequest;
import com.english.content_service.dto.request.GrammarTestRequest;
import com.english.content_service.dto.request.GrammarTopicRequest;
import com.english.content_service.dto.response.*;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface GrammarService {
    //topic
    Page<GrammarTopicResponse> getTopics(int page, int size);
    GrammarTopicResponse addTopic(GrammarTopicRequest topic, MultipartFile imageFile);

    //grammar
    GetGrammarTopicResponse getGrammarsByTopicId(String topicId);
    GrammarResponse addGrammar(String topicId, GrammarRequest request);

    //test
    List<GrammarTestQuestionResponse> getTestQuestionsByTestId(String testId);
    GetTestsByGrammarIdResponse getTestsByGrammarId(String grammarId, int page, int size);
    GrammarTestResponse addTest(String grammarTopic, GrammarTestRequest request);
}
