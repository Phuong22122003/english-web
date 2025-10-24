package com.english.content_service.service.implt;

import com.english.content_service.dto.request.GrammarRequest;
import com.english.content_service.dto.request.GrammarTestRequest;
import com.english.content_service.dto.request.GrammarTopicRequest;
import com.english.content_service.entity.Grammar;
import com.english.content_service.entity.GrammarTest;
import com.english.content_service.entity.GrammarTestQuestion;
import com.english.content_service.entity.GrammarTopic;
import com.english.content_service.mapper.GrammarMapper;
import com.english.content_service.repository.GrammarRepository;
import com.english.content_service.repository.GrammarTestQuestionRepository;
import com.english.content_service.repository.GrammarTestRepository;
import com.english.content_service.repository.GrammarTopicRepository;
import com.english.content_service.service.AgentService;
import com.english.dto.response.*;
import com.english.enums.RequestType;
import com.english.exception.NotFoundException;
import com.english.service.FileService;
import jakarta.transaction.Transactional;
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
import java.util.*;

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
    AgentService agentService;

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
    public GetGrammarTestQuestionsByTestIdResponse getTestQuestionsByTestId(String testId) {
        List<GrammarTestQuestion> questions = grammarTestQuestionRepository.findByTestId(testId);
        GrammarTest grammarTest = null;
        if (questions.size() > 0) {
            grammarTest = questions.getFirst().getTest();
        }
        return GetGrammarTestQuestionsByTestIdResponse.builder()
                .duration(grammarTest.getDuration())
                .grammarTestQuestions(grammarMapper.toGrammarTestQuestionResponses(questions))
                .testName(grammarTest.getName())
                .testId(grammarTest.getId())
                .grammarName(grammarTest.getGrammar().getTitle())
                .grammarId(grammarTest.getGrammar().getId())
                .build();
    }

    @Override
    public List<GrammarTestResponse> getTestsByIds(List<String> ids) {
        List<GrammarTest> tests = grammarTestRepository.findAllById(ids);
        return grammarMapper.toGrammarTestResponses(tests);
    }

    @Override
    @Transactional
    public void deleteTestById(String id) {
        if (!grammarTestRepository.existsById(id)) {
            throw new RuntimeException("Grammar test not found");
        }
        grammarTestQuestionRepository.deleteByTestId(id);
        grammarTestRepository.deleteById(id);
    }



    @Override
    @Transactional
    public GrammarTestResponse updateGrammarTest(String testId, GrammarTestRequest request) {
        GrammarTest test = grammarTestRepository.findById(testId).orElseThrow(()->new NotFoundException("Test not found"));
        test.setName(request.getName());
        test.setDuration(request.getDuration());

        List<String> deleteIds = new ArrayList<>();
        List<GrammarTestQuestion> newQuestion= new ArrayList<>();
        List<GrammarTestQuestion> questions = grammarTestQuestionRepository.findByTestId(testId);
        Map<String,GrammarTestQuestion> idToQuestion = new HashMap<>();
        for(var question: questions){
            idToQuestion.put(question.getId(),question);
        }
        for(var question: request.getQuestions()){
            if(question.getAction().equals(RequestType.ADD)){
                var q = grammarMapper.toGrammarTestQuestion(question);
                q.setId(null);
                q.setTest(test);
                newQuestion.add(q);
            }
            else if(question.getAction().equals(RequestType.UPDATE)){
                var q = idToQuestion.get(question.getId());
                grammarMapper.updateGrammarTestQuestionPartial(q, question);
            }
            else if(question.getAction().equals(RequestType.DELETE)){
                deleteIds.add(question.getId());
            }
        }
        grammarTestQuestionRepository.deleteAllById(deleteIds);


        grammarTestRepository.save(test);
        grammarTestQuestionRepository.saveAll(newQuestion);
        return null;
    }

    @Override
    @Transactional
    public GrammarTopicResponse addTopic(GrammarTopicRequest request, MultipartFile imageFile) {
        GrammarTopic topic = new GrammarTopic();
        topic.setCreatedAt(LocalDateTime.now());
        topic.setName(request.getName());
        topic.setDescription(request.getDescription());
        if(imageFile!=null && !imageFile.isEmpty()){
            FileResponse fileResponse = fileService.uploadImage(imageFile);
            topic.setImageUrl(fileResponse.getUrl());
            topic.setPublicId(fileResponse.getPublicId());
        }
        try {
            topic = grammarTopicRepository.save(topic);
            agentService.addTopicToVectorDB(topic);
        } catch (Exception e) {
            if(topic.getPublicId()!=null) fileService.deleteFile(topic.getPublicId());
            throw new RuntimeException(e);
        }
        return grammarMapper.toGrammarTopicResponse(topic);
    }

    @Override
    public List<GrammarTopicResponse> getTopicsByIds(List<String> ids) {
        List<GrammarTopic> topics = grammarTopicRepository.findAllById(ids);
        return grammarMapper.toGrammarTopicResponses(topics);
    }

    @Override
    @Transactional
    public GrammarTopicResponse updateTopic(String topicId, GrammarTopicRequest request, MultipartFile image) {
        GrammarTopic topic = grammarTopicRepository.findById(topicId).orElseThrow(()->new NotFoundException("Topic not found"));
        topic.setName(request.getName());
        topic.setDescription(request.getDescription());
        if(image!=null && !image.isEmpty()){
            FileResponse fileResponse;
            if(topic.getPublicId()!=null){
                fileResponse = fileService.uploadImage(image,topic.getPublicId());
            }
            else{
                fileResponse = fileService.uploadImage(image);
            }
            topic.setPublicId(fileResponse.getPublicId());
            topic.setImageUrl(fileResponse.getUrl());
        }
        grammarTopicRepository.save(topic);
        return grammarMapper.toGrammarTopicResponse(topic);
    }

    @Override
    @Transactional
    public void deleteTopicById(String topicId) {
        // Lấy tất cả grammar id trong topic
        List<Grammar> grammars = grammarRepository.findByTopicId(topicId);
        List<String> grammarIds = grammars.stream().map(Grammar::getId).toList();

        if (!grammarIds.isEmpty()) {
            // Lấy tất cả test id thuộc các grammar này
            List<GrammarTest> tests = grammarTestRepository.findAll().stream()
                    .filter(t -> grammarIds.contains(t.getGrammar().getId()))
                    .toList();

            List<String> testIds = tests.stream().map(GrammarTest::getId).toList();

            // Xóa theo thứ tự: Question → Test → Grammar
            if (!testIds.isEmpty()) {
                grammarTestQuestionRepository.deleteByTestIds(testIds);
                grammarTestRepository.deleteByGrammarIds(grammarIds);
            }

            grammarRepository.deleteByTopicId(topicId);
        }

        grammarTopicRepository.deleteById(topicId);
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

    @Override
    public GrammarResponse updateGrammar(String grammarId, GrammarRequest request) {
        Grammar grammar = grammarRepository.findById(grammarId).orElseThrow(()->new NotFoundException("Grammar not found"));
        grammar.setContent(request.getContent());
        grammar.setTitle(request.getTitle());
        return grammarMapper.toGrammarResponse(grammar);
    }

    @Override
    @Transactional
    public void deleteGrammarById(String grammarId) {
        // Lấy danh sách test id trước
        List<GrammarTest> tests = grammarTestRepository.findByGrammarId(grammarId);
        List<String> testIds = tests.stream().map(GrammarTest::getId).toList();

        // Xóa tất cả câu hỏi theo danh sách testId
        if (!testIds.isEmpty()) {
            grammarTestQuestionRepository.deleteByTestIds(testIds);
            grammarTestRepository.deleteByGrammarId(grammarId);
        }

        grammarRepository.deleteById(grammarId);
    }


    @Override
    @Transactional
    public GrammarTestResponse addTest(String grammarId,GrammarTestRequest request) {
        Grammar grammar = grammarRepository.findById(grammarId).orElseThrow(()->new RuntimeException("Grammar not found"));
        GrammarTest test = grammarMapper.toGrammarTest(request);
        test.setGrammar(grammar);
        test.setCreatedAt(LocalDateTime.now());
        test = grammarTestRepository.save(test);
        List<GrammarTestQuestion> questions = grammarMapper.toGrammarTestQuestion(request.getQuestions());
        for(var question: questions){
            question.setTest(test);
        }
        grammarTestQuestionRepository.saveAll(questions);
        GrammarTestResponse grammarTestResponse = grammarMapper.toGrammarTestResponse(test);
        grammarTestResponse.setQuestions(grammarMapper.toGrammarTestQuestionResponses(questions));
        return  grammarTestResponse;
    }
}
