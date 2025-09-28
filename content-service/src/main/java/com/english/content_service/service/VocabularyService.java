package com.english.content_service.service;
import java.util.List;

import com.english.content_service.dto.request.VocabTopicRequest;
import com.english.content_service.dto.request.VocabularyRequest;
import com.english.content_service.dto.request.VocabularyTestRequest;
import com.english.content_service.dto.response.*;
import com.english.content_service.entity.Vocabulary;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface VocabularyService {
    public Page<VocabTopicResponse> getTopics(int page, int size);
    public GetVocabularyTopicResponse getVocabulariesByTopicId(String topicId);
    public Page<VocabularyTestResponse> getTestsByTopicId(String topicId, int page, int size);
    public List<VocabularyTestQuestionResponse> getTestQuestionsByTestId(String testId);
    public VocabTopicResponse addTopic(VocabTopicRequest request, MultipartFile imageFile);
    public VocabTopicResponse updateTopic(String topicId, VocabTopicRequest request, MultipartFile imageFile);
    public void deleteTopic(String topicId);
    public List<VocabularyResponse> addVocabularies(String topicId, List<VocabularyRequest> requests, List<MultipartFile> imageFiles, List<MultipartFile> audioFiles);
    public VocabularyResponse updateVocabulary(String vocabId, VocabularyRequest request, MultipartFile imageFile, MultipartFile audioFile);
    public void deleteVocabulary(String vocabId);
    public void addTest(String topicId,VocabularyTestRequest vocabularyTestRequest, List<MultipartFile> imageFiles);
}
