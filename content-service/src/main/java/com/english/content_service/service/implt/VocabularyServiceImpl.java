package com.english.content_service.service.implt;

import java.util.List;

import org.springframework.stereotype.Service;

import com.english.content_service.dto.response.VocabTopicResponse;
import com.english.content_service.dto.response.VocabularyResponse;
import com.english.content_service.dto.response.VocabularyTestQuestionResponse;
import com.english.content_service.dto.response.VocabularyTestResponse;
import com.english.content_service.entity.Vocabulary;
import com.english.content_service.entity.VocabularyTest;
import com.english.content_service.entity.VocabularyTestQuestion;
import com.english.content_service.entity.VocabularyTopic;
import com.english.content_service.mapper.VocabularyMapper;
import com.english.content_service.repository.VocabularyRepository;
import com.english.content_service.repository.VocabularyTestQuestionRepository;
import com.english.content_service.repository.VocabularyTestRepository;
import com.english.content_service.repository.VocabularyTopicRepository;
import com.english.content_service.service.VocabularyService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VocabularyServiceImpl implements VocabularyService {
    VocabularyTopicRepository vocabularyTopicRepository;
    VocabularyRepository vocabularyRepository;
    VocabularyTestRepository vocabularyTestRepository;
    VocabularyTestQuestionRepository vocabularyTestQuestionRepository;
    VocabularyMapper vocabularyMapper;
    @Override
    public Page<VocabTopicResponse> getTopics(int page, int size) {
        Page<VocabularyTopic> topics = vocabularyTopicRepository.findAll(PageRequest.of(page, size));
        List<VocabularyTopic> topicList = topics.getContent();
        List<VocabTopicResponse> topicResponses = vocabularyMapper.toVocabTopicResponses(topicList);
        return new PageImpl<>(topicResponses, PageRequest.of(page, size), topics.getTotalElements());
    }
    @Override
    public Page<VocabularyResponse> getVocabulariesByTopicId(String topicId, int page, int size) {
        Page<Vocabulary> vocabularies = vocabularyRepository.findByTopicId(topicId, PageRequest.of(page, size));
        List<Vocabulary> vocabularyList = vocabularies.getContent();
        List<VocabularyResponse> vocabularyResponses = vocabularyMapper.toVocabularyResponses(vocabularyList);
        return new PageImpl<>(vocabularyResponses, PageRequest.of(page, size), vocabularies.getTotalElements());
    }
    @Override
    public Page<VocabularyTestResponse> getTestsByTopicId(String topicId, int page, int size) {
        Page<VocabularyTest> tests = vocabularyTestRepository.findByTopicId(topicId, PageRequest.of(page, size));
        List<VocabularyTest> testList = tests.getContent();
        List<VocabularyTestResponse> testResponses = vocabularyMapper.toVocabularyTestResponses(testList);
        return new PageImpl<>(testResponses, PageRequest.of(page, size), tests.getTotalElements());
    }
    @Override
    public List<VocabularyTestQuestionResponse> getTestQuestionsByTestId(String testId) {
        List<VocabularyTestQuestion> questions = vocabularyTestQuestionRepository.findByTestId(testId);
        return vocabularyMapper.toVocabularyTestQuestionResponses(questions);
    }
}
