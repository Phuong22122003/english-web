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
    Page<GrammarTopicResponse> getTopics(int page, int size);

    GetGrammarTopicResponse getGrammarsByTopicId(String topicId);

    GetTestsByGrammarIdResponse getTestsByGrammarId(String grammarId, int page, int size);

    List<GrammarTestQuestionResponse> getTestQuestionsByTestId(String testId);

    GrammarTopicResponse addTopic(GrammarTopicRequest topic, MultipartFile imageFile);

    GrammarResponse addGrammar(String topicId, GrammarRequest request);

    GrammarTestResponse addTest(String grammarTopic, GrammarTestRequest request);
}
