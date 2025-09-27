package com.english.content_service.service;

import com.english.content_service.dto.response.GetGrammarTopicResponse;
import com.english.content_service.dto.response.GrammarTestQuestionResponse;
import com.english.content_service.dto.response.GrammarTestResponse;
import com.english.content_service.dto.response.GrammarTopicResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface GrammarService {
    Page<GrammarTopicResponse> getTopics(int page, int size);

    GetGrammarTopicResponse getGrammarsByTopicId(String topicId);

    Page<GrammarTestResponse> getTestsByGrammarId(String grammarId, int page, int size);

    List<GrammarTestQuestionResponse> getTestQuestionsByTestId(String testId);
}
