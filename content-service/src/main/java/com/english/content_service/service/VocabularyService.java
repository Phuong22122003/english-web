package com.english.content_service.service;
import java.util.List;

import com.english.content_service.dto.response.*;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface VocabularyService {
    public Page<VocabTopicResponse> getTopics(int page, int size);
    public GetVocabularyTopicResponse getVocabulariesByTopicId(String topicId);
    public Page<VocabularyTestResponse> getTestsByTopicId(String topicId, int page, int size);
    public List<VocabularyTestQuestionResponse> getTestQuestionsByTestId(String testId);
}
