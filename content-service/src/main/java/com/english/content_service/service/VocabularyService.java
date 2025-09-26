package com.english.content_service.service;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.english.content_service.dto.response.VocabTopicResponse;
import com.english.content_service.dto.response.VocabularyResponse;
import com.english.content_service.dto.response.VocabularyTestQuestionResponse;
import com.english.content_service.dto.response.VocabularyTestResponse;

@Service
public interface VocabularyService {
    public Page<VocabTopicResponse> getTopics(int page, int size);
    public Page<VocabularyResponse> getVocabulariesByTopicId(String topicId, int page, int size);
    public Page<VocabularyTestResponse> getTestsByTopicId(String topicId, int page, int size);
    public List<VocabularyTestQuestionResponse> getTestQuestionsByTestId(String testId);
}
