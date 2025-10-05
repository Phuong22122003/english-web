package com.english.content_service.service;
import java.util.List;

import com.english.content_service.dto.request.VocabTopicRequest;
import com.english.content_service.dto.request.VocabularyRequest;
import com.english.content_service.dto.request.VocabularyTestRequest;

import com.english.dto.response.*;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface VocabularyService {
    //topic
    public Page<VocabTopicResponse> getTopics(int page, int size);
    public VocabTopicResponse addTopic(VocabTopicRequest request, MultipartFile imageFile);
    public VocabTopicResponse updateTopic(String topicId, VocabTopicRequest request, MultipartFile imageFile);
    public void deleteTopic(String topicId);

    //vocab
    public GetVocabularyTopicResponse getVocabulariesByTopicId(String topicId);
    public List<VocabularyResponse> addVocabularies(String topicId, List<VocabularyRequest> requests, List<MultipartFile> imageFiles, List<MultipartFile> audioFiles);
    public VocabularyResponse updateVocabulary(String vocabId, VocabularyRequest request, MultipartFile imageFile, MultipartFile audioFile);
    public void deleteVocabulary(String vocabId);

    //test
    public GetTestsVocabByTopicIdResponse getTestsByTopicId(String topicId, int page, int size);
    public GetVocabularyTestQuestionResponse getTestQuestionsByTestId(String testId);
    public VocabularyTestResponse addTest(String topicId,VocabularyTestRequest vocabularyTestRequest, List<MultipartFile> imageFiles);
    public List<VocabularyTestResponse> getTestsByIds(List<String> ids);
}
