package com.english.content_service.service;

import com.english.content_service.dto.request.GrammarRequest;
import com.english.content_service.dto.request.GrammarTestRequest;
import com.english.content_service.dto.request.GrammarTopicRequest;
import com.english.dto.response.*;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface GrammarService {
    //topic
    Page<GrammarTopicResponse> getTopics(int page, int size);
    GrammarTopicResponse addTopic(GrammarTopicRequest topic, MultipartFile imageFile);
    List<GrammarTopicResponse> getTopicsByIds(List<String> ids);
    GrammarTopicResponse updateTopic(String topicId, GrammarTopicRequest request, MultipartFile image);
    void deleteTopicById(String id);
    //grammar
    GetGrammarTopicResponse getGrammarsByTopicId(String topicId);
    GrammarResponse addGrammar(String topicId, GrammarRequest request);
    GrammarResponse updateGrammar(String grammarId,GrammarRequest request);
    void deleteGrammarById(String id);
    //test
    GetTestsByGrammarIdResponse getTestsByGrammarId(String grammarId, int page, int size);
    GrammarTestResponse addTest(String grammarTopic, GrammarTestRequest request);
    GetGrammarTestQuestionsByTestIdResponse getTestQuestionsByTestId(String testId);
    List<GrammarTestResponse> getTestsByIds(List<String> ids);
    void deleteTestById(String id);
    GrammarTestResponse updateGrammarTest(String testId, GrammarTestRequest request);;
}
