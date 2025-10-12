package com.english.content_service.service.implt;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.english.content_service.dto.request.VocabTopicRequest;
import com.english.content_service.dto.request.VocabularyRequest;
import com.english.content_service.dto.request.VocabularyTestRequest;
import com.english.dto.response.*;
import com.english.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
import com.english.dto.response.*;
import com.english.service.FileService;

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
    FileService fileService;
    @Override
    public Page<VocabTopicResponse> getTopics(int page, int size) {
        Page<VocabularyTopic> topics = vocabularyTopicRepository.findAll(PageRequest.of(page, size));
        List<VocabularyTopic> topicList = topics.getContent();
        List<VocabTopicResponse> topicResponses = vocabularyMapper.toVocabTopicResponses(topicList);
        return new PageImpl<>(topicResponses, PageRequest.of(page, size), topics.getTotalElements());
    }
    @Override
    public GetVocabularyTopicResponse getVocabulariesByTopicId(String topicId) {
        List<Vocabulary> vocabularies = vocabularyRepository.findByTopicId(topicId);
        Optional<VocabularyTopic> topicOpt = vocabularyTopicRepository.findById(topicId);
        return topicOpt.map(vocabularyTopic -> GetVocabularyTopicResponse.builder()
                .name(vocabularyTopic.getName())
                .topicId(vocabularyTopic.getId())
                .vocabularies(vocabularyMapper.toVocabularyResponses(vocabularies))
                .build()).orElse(null);
    }
    @Override
    public GetTestsVocabByTopicIdResponse getTestsByTopicId(String topicId, int page, int size) {
        Page<VocabularyTest> tests = vocabularyTestRepository.findByTopicId(topicId, PageRequest.of(page, size));
        List<VocabularyTest> testList = tests.getContent();
        List<VocabularyTestResponse> testResponses = vocabularyMapper.toVocabularyTestResponses(testList);
        Optional<VocabularyTopic> topicOtp = vocabularyTopicRepository.findById(topicId);
        return topicOtp.map(topic -> GetTestsVocabByTopicIdResponse.builder()
                .vocabularyTests(new PageImpl<>(testResponses, PageRequest.of(page, size), tests.getTotalElements()))
                .topicName(topic.getName())
                .topicId(topicId)
                .build()).orElse(null);
    }
    @Override
    public GetVocabularyTestQuestionResponse getTestQuestionsByTestId(String testId) {
        List<VocabularyTestQuestion> questions = vocabularyTestQuestionRepository.findByTestId(testId);
        Optional<VocabularyTest> vocabularyTestOtp = vocabularyTestRepository.findById(testId);
        return  vocabularyTestOtp.map(vocabularyTest -> GetVocabularyTestQuestionResponse.builder()
                .topicName(vocabularyTest.getTopic().getName())
                .topicId(vocabularyTest.getTopic().getId())
                .duration(vocabularyTest.getDuration())
                .testId(vocabularyTest.getId())
                .testName(vocabularyTest.getName())
                .questions(vocabularyMapper.toVocabularyTestQuestionResponses(questions))
                .build()).orElse(null);
    }
    @Override
    public VocabTopicResponse addTopic(VocabTopicRequest request, MultipartFile imageFile) {
        VocabularyTopic topic = vocabularyMapper.toVocabTopic(request);
        FileResponse fileResponse=null;
        if (imageFile != null && !imageFile.isEmpty()) {
            fileResponse = fileService.uploadImage(imageFile);
            topic.setImageUrl(fileResponse.getUrl());
            topic.setPublicId(fileResponse.getPublicId());
        }
        topic.setCreatedAt(LocalDateTime.now());
        try{
            VocabularyTopic savedTopic = vocabularyTopicRepository.save(topic);
            return vocabularyMapper.toVocabTopicResponse(savedTopic);
        } catch (Exception e) {
            if(fileResponse!=null)
                fileService.deleteFile(fileResponse.getPublicId());
            throw new RuntimeException(e);
        }
    }
    @Override
    public VocabTopicResponse updateTopic(String topicId, VocabTopicRequest request, MultipartFile imageFile) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateTopic'");
    }
    @Override
    // admim
    // only delete new topic
    public void deleteTopic(String topicId) {
        VocabularyTopic topic = this.vocabularyTopicRepository.findById(topicId).orElseThrow(()->{
            return new NotFoundException("Topic not found");
        });
        this.vocabularyTopicRepository.deleteById(topicId);
        this.fileService.deleteFile(topic.getPublicId());
    }

    @Override
    public List<VocabTopicResponse> getTopicsByIds(List<String> ids) {
        return vocabularyMapper.toVocabTopicResponses(vocabularyTopicRepository.findAllById(ids));
    }

    @Override
    public List<VocabularyResponse> addVocabularies(String topicId, List<VocabularyRequest> requests,
            List<MultipartFile> imageFiles, List<MultipartFile> audioFiles) {
        VocabularyTopic topic = this.vocabularyTopicRepository.findById(topicId).orElseThrow(()-> new RuntimeException("Topic not found"));
        List<Vocabulary> vocabularies = vocabularyMapper.toVocabularies(requests);
        List<String> publicIds = new ArrayList<>();
        try{
            for(int i = 0; i< vocabularies.size();i++){
                Vocabulary v = vocabularies.get(i);
                v.setTopic(topic);
                v.setCreatedAt(LocalDateTime.now());
                FileResponse fileResponse = fileService.uploadImage(imageFiles.get(i));
                v.setImageUrl(fileResponse.getUrl());
                v.setPublicImageId(fileResponse.getPublicId());
                fileResponse = fileService.uploadAudio(audioFiles.get(i));
                v.setAudioUrl(fileResponse.getUrl());
                v.setPublicAudioId(fileResponse.getPublicId());
                publicIds.add(v.getPublicAudioId());
                publicIds.add(v.getAudioUrl());
            }
            vocabularyRepository.saveAll(vocabularies);
        } catch (Exception e) {
            for(String publicId: publicIds){
                fileService.deleteFile(publicId);
            }
            throw new RuntimeException(e);
        }
        return vocabularyMapper.toVocabularyResponses(vocabularies);
    }
    @Override
    public VocabularyResponse updateVocabulary(String vocabId, VocabularyRequest request, MultipartFile imageFile,
                                               MultipartFile audioFile) {
       Vocabulary vocabulary = this.vocabularyRepository.findById(vocabId).orElseThrow(()->new NotFoundException("Vocab not found"));
       this.vocabularyMapper.patchUpdate(vocabulary,request);
       if(imageFile!=null&&!imageFile.isEmpty()){
           if(vocabulary.getPublicImageId()!=null){
               this.fileService.uploadImage(imageFile,vocabulary.getPublicImageId());
           }
           else{
               this.fileService.uploadImage(imageFile);
           }
       }
       if(audioFile!=null&&!audioFile.isEmpty()){
           if(vocabulary.getPublicAudioId()!=null){
               this.fileService.uploadAudio(audioFile,vocabulary.getPublicAudioId());
           }
           else{
               this.fileService.uploadAudio(audioFile);
           }
       }
        return vocabularyMapper.toVocabularyResponse(vocabulary);
    }
    @Override
    public void deleteVocabulary(String vocabId) {
        this.vocabularyRepository.deleteById(vocabId);
    }

    @Override
    public VocabularyTestResponse addTest(String topicId, VocabularyTestRequest vocabularyTestRequest, List<MultipartFile> imageFiles) {
        VocabularyTopic topic = vocabularyTopicRepository.findById(topicId).orElseThrow(()-> new RuntimeException("Topic not found"));
        VocabularyTest test =  VocabularyTest
                .builder()
                .topic(topic)
                .name(vocabularyTestRequest.getName())
                .duration(vocabularyTestRequest.getDuration())
                .createdAt(LocalDateTime.now())
                .build();
        test = vocabularyTestRepository.save(test);
        List<VocabularyTestQuestion> questions = vocabularyMapper.toVocabularyTestQuestions(vocabularyTestRequest.getQuestions());
        List<String> publicIds = new ArrayList<>();
        VocabularyTestResponse vocabularyTestResponse;
        try{
            for(int i = 0; i< questions.size();i++){
                VocabularyTestQuestion q = questions.get(i);
                q.setTest(test);
                if(imageFiles!=null && imageFiles.size()>i && imageFiles.get(i)!=null && !imageFiles.get(i).isEmpty()){
                    FileResponse fileResponse = fileService.uploadImage(imageFiles.get(i));
                    q.setImageUrl(fileResponse.getUrl());
                    q.setPublicId(fileResponse.getPublicId());
                    publicIds.add(q.getPublicId());
                }
            }
            questions = vocabularyTestQuestionRepository.saveAll(questions);
        } catch (Exception e) {
            for(String publicId: publicIds){
                fileService.deleteFile(publicId);
            }
            throw new RuntimeException(e);
        }
        vocabularyTestResponse = vocabularyMapper.toVocabularyTestResponse(test);
        vocabularyTestResponse.setQuestions(vocabularyMapper.toVocabularyTestQuestionResponses(questions));
        return  vocabularyTestResponse;
    }

    @Override
    public List<VocabularyTestResponse> getTestsByIds(List<String> ids) {
        List<VocabularyTest> tests = vocabularyTestRepository.findAllById(ids);
        return vocabularyMapper.toVocabularyTestResponses(tests);
    }
}
