package com.english.content_service.service.implt;

import com.english.content_service.dto.request.GrammarRequest;
import com.english.content_service.dto.request.GrammarTopicRequest;
import com.english.content_service.dto.response.*;
import com.english.content_service.entity.Grammar;
import com.english.content_service.entity.GrammarTest;
import com.english.content_service.entity.GrammarTestQuestion;
import com.english.content_service.entity.GrammarTopic;
import com.english.content_service.mapper.GrammarMapper;
import com.english.content_service.repository.GrammarRepository;
import com.english.content_service.repository.GrammarTestQuestionRepository;
import com.english.content_service.repository.GrammarTestRepository;
import com.english.content_service.repository.GrammarTopicRepository;
import com.english.dto.FileResponse;
import com.english.service.FileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import com.english.content_service.service.GrammarService;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GrammarServiceImpl implements GrammarService {
    GrammarTopicRepository grammarTopicRepository;
    GrammarRepository grammarRepository;
    GrammarTestRepository grammarTestRepository;
    GrammarTestQuestionRepository grammarTestQuestionRepository;
    GrammarMapper grammarMapper;
    FileService fileService;

    @Override
    public Page<GrammarTopicResponse> getTopics(int page, int size) {
        Page<GrammarTopic> topics = grammarTopicRepository.findAll(PageRequest.of(page, size));
        List<GrammarTopic> topicList = topics.getContent();
        List<GrammarTopicResponse> topicResponses = grammarMapper.toGrammarTopicResponses(topicList);
        return new PageImpl<>(topicResponses, PageRequest.of(page, size), topics.getTotalElements());
    }

    @Override
    public GetGrammarTopicResponse getGrammarsByTopicId(String topicId) {
        List<Grammar> grammars = grammarRepository.findByTopicId(topicId);
        Optional<GrammarTopic> topicOpt = grammarTopicRepository.findById(topicId);

        return topicOpt.map(topic -> GetGrammarTopicResponse.builder()
                        .topicId(topic.getId())
                        .name(topic.getName())
                        .grammars(grammarMapper.toGrammarResponses(grammars))
                        .build())
                .orElse(null);
    }

    @Override
    public GetTestsByGrammarIdResponse getTestsByGrammarId(String grammarId, int page, int size) {
        Page<GrammarTest> tests = grammarTestRepository.findByGrammarId(grammarId, PageRequest.of(page, size));
        List<GrammarTestResponse> testResponses = grammarMapper.toGrammarTestResponses(tests.getContent());
        Optional<Grammar> grammarOtp = grammarRepository.findById(grammarId);
        return grammarOtp.map(grammar -> GetTestsByGrammarIdResponse.builder()
                .grammarTests(new PageImpl<>(testResponses, PageRequest.of(page, size), tests.getTotalElements()))
                .grammarName(grammar.getTitle())
                .grammarId(grammar.getId())
                .build()).orElse(null);
    }

    @Override
    public List<GrammarTestQuestionResponse> getTestQuestionsByTestId(String testId) {
        List<GrammarTestQuestion> questions = grammarTestQuestionRepository.findByTestId(testId);
        return grammarMapper.toGrammarTestQuestionResponses(questions);
    }

    @Override
    public GrammarTopicResponse addTopic(GrammarTopicRequest request, MultipartFile imageFile) {
        GrammarTopic topic = new GrammarTopic();
        topic.setCreatedAt(LocalDateTime.now());
        topic.setName(request.getName());
        topic.setDescription(request.getDescription());
        FileResponse fileResponse = fileService.uploadImage(imageFile);
        topic.setImageUrl(fileResponse.getUrl());
        topic.setPublicId(fileResponse.getPublicId());
        grammarTopicRepository.save(topic);
        return grammarMapper.toGrammarTopicResponse(topic);
    }

    @Override
    public GrammarResponse addGrammar(String topicId, GrammarRequest request) {
        GrammarTopic topic = this.grammarTopicRepository.findById(topicId).orElseThrow(()->new RuntimeException("Grammar topic not found"));
        Grammar grammar = Grammar
                .builder()
                .title(request.getTitle())
                .content(request.getContent())
                .topic(topic)
                .createdAt(LocalDateTime.now())
                .build();
        return grammarMapper.toGrammarResponse(grammar);
    }
}
