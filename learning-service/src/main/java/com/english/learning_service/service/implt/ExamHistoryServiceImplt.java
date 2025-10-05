package com.english.learning_service.service.implt;

import com.english.dto.response.*;
import com.english.exception.NotFoundException;
import com.english.learning_service.dto.request.ExamHistoryRequest;
import com.english.learning_service.dto.response.ExamHistoryResponse;
import com.english.learning_service.dto.response.QuestionResponse;
import com.english.learning_service.entity.ExamHistory;
import com.english.learning_service.entity.UserAnswer;
import com.english.learning_service.enums.FilterType;
import com.english.learning_service.enums.ItemTypeEnum;
import com.english.learning_service.httpclient.GrammarClient;
import com.english.learning_service.httpclient.ListeningClient;
import com.english.learning_service.httpclient.VocabularyClient;
import com.english.learning_service.mapper.ExamHistoryMapper;
import com.english.learning_service.repository.ExamHistoryRepository;
import com.english.learning_service.repository.UserAnswerRepository;
import com.english.learning_service.service.ExamHistoryService;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Data
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ExamHistoryServiceImplt implements ExamHistoryService {
    private ExamHistoryMapper examHistoryMapper;
    private ExamHistoryRepository examHistoryRepository;
    private UserAnswerRepository userAnswerRepository;
    private VocabularyClient vocabularyClient;
    private GrammarClient grammarClient;
    private ListeningClient listeningClient;
    @Override
    public ExamHistoryResponse addExamHistory(ExamHistoryRequest request) {
        var context = SecurityContextHolder.getContext();
        String userId = context.getAuthentication().getName();
        ExamHistory examHistory = examHistoryMapper.toExamHistory(request);
        examHistory.setUserId(userId);
        examHistoryRepository.save(examHistory);
        return examHistoryMapper.toExamHistoryResponse(examHistory);
    }

    @Override
    public Page<ExamHistoryResponse> getExamHistories(int page, int limit, FilterType filterType) {
        var context = SecurityContextHolder.getContext();
        String userId = context.getAuthentication().getName();
        var pageable = org.springframework.data.domain.PageRequest.of(page, limit);
        Page<ExamHistory> histories;
        if(filterType.equals(FilterType.ALL)){
            histories = examHistoryRepository.findByUserId(userId, pageable);
        }
        else{
            histories = examHistoryRepository.findByUserIdAndTestType(userId,ItemTypeEnum.map(filterType),pageable);
        }


        List<String> grammarIds = new ArrayList<>();
        List<String> listeningIds = new ArrayList<>();
        List<String> vocabIds = new ArrayList<>();

        for(ExamHistory history: histories.getContent()){
            if(history.getTestType().equals(ItemTypeEnum.GRAMMAR)){
                grammarIds.add(history.getTestId());
            } else if (history.getTestType().equals(ItemTypeEnum.LISTENING)) {
                listeningIds.add(history.getTestId());
            } else if (history.getTestType().equals(ItemTypeEnum.VOCABULARY)) {
                vocabIds.add(history.getTestId());
            }
        }
        Map<String, Object> idToTest = new HashMap<>();
        if(!vocabIds.isEmpty()){
            List<VocabularyTestResponse> vocabularyResponses = vocabularyClient.getTestsByIds(vocabIds);
            for(var vocab: vocabularyResponses){
                idToTest.put(vocab.getId(),vocab);
            }
        }
        if(!grammarIds.isEmpty()){
            List<GrammarTestResponse> grammarTestResponses = grammarClient.getTestsByIds(grammarIds);
            for(var grammar: grammarTestResponses){
                idToTest.put(grammar.getId(),grammar);
            }
        }
        if(!listeningIds.isEmpty()){
            List<ListeningTestReponse> listeningTestReponses = listeningClient.getTestsByIds(listeningIds);
            for(var listening: listeningTestReponses){
                idToTest.put(listening.getId(),listening);
            }
        }
        List<ExamHistoryResponse> examHistoryResponses = examHistoryMapper.toExamHistoryResponses(histories.getContent());
        for(var h: examHistoryResponses){
            Object test = idToTest.get(h.getTestId());
            if(h.getTestType().equals(ItemTypeEnum.VOCABULARY)){
                var vocab = (VocabularyTestResponse) test;
                h.setDuration(vocab.getDuration());
                h.setName(vocab.getName());
            }else if(h.getTestType().equals(ItemTypeEnum.GRAMMAR)){
                var grammar = (GrammarTestResponse) test;
                h.setDuration(grammar.getDuration());
                h.setName(grammar.getName());
            }else if(h.getTestType().equals(ItemTypeEnum.LISTENING)){
                var listening = (ListeningTestReponse) test;
                h.setDuration(listening.getDuration());
                h.setName(listening.getName());
            }
        }
        return new PageImpl<>(examHistoryResponses,histories.getPageable(),histories.getTotalElements());
    }


    @Override
    public ExamHistoryResponse getExamHistoryDetail(String examHistoryId) {
        ExamHistory examHistory = examHistoryRepository.findById(examHistoryId).orElseThrow(()-> new NotFoundException("Exam history not found"));
        ExamHistoryResponse examHistoryResponse = examHistoryMapper.toExamHistoryResponse(examHistory);
        List<UserAnswer> userAnswers = userAnswerRepository.findByExamHistoryId(examHistoryId);
        Map<String,UserAnswer> idToAnswer = new HashMap<>();
        for(var answer: userAnswers){
            idToAnswer.put(answer.getQuestionId(),answer);
        }
        List<QuestionResponse> questions = new ArrayList<>();
        switch (examHistory.getTestType()){
            case ItemTypeEnum.VOCABULARY -> {
                GetVocabularyTestQuestionResponse vocab = vocabularyClient.getTestQuestionsByTestId(examHistory.getTestId());
                examHistoryResponse.setName(vocab.getTestName());
                examHistoryResponse.setDuration(vocab.getDuration());
                for(var q: vocab.getQuestions()){
                    questions.add(QuestionResponse.builder()
                                    .options(q.getOptions())
                                    .correctAnswer(q.getCorrectAnswer())
                                    .questionOrder(q.getQuestionOrder())
                                    .userAnswer(idToAnswer.get(q.getId()).getSelectedAnswer())
                                    .question(q.getQuestion())
                                    .explanation(q.getExplaination())
                            .build());
                }
                break;
            }
            case ItemTypeEnum.GRAMMAR -> {
                GetGrammarTestQuestionsByTestIdResponse grammar = grammarClient.getTestQuestionsByTestId(examHistory.getTestId());
                examHistoryResponse.setName(grammar.getTestName());
                examHistoryResponse.setDuration(grammar.getDuration());
                for(var q: grammar.getGrammarTestQuestions()){
                    questions.add(QuestionResponse.builder()
                            .options(q.getOptions())
                            .correctAnswer(q.getCorrectAnswer())
                            .questionOrder(q.getQuestionOrder())
                            .userAnswer(idToAnswer.get(q.getId()).getSelectedAnswer())
                            .question(q.getQuestion())
                                    .explanation(q.getExplaination())
                            .build());
                }
                break;
            }
            case ItemTypeEnum.LISTENING -> {
                ListeningTestReponse listening = listeningClient.getTestDetail(examHistory.getTestId());
                examHistoryResponse.setName(listening.getName());
                examHistoryResponse.setDuration(listening.getDuration());
                for(var q: listening.getQuestions()){
                    questions.add(QuestionResponse.builder()
                            .options(q.getOptions())
                            .correctAnswer(q.getCorrectAnswer())
                            .questionOrder(q.getQuestionOrder())
                            .userAnswer(idToAnswer.get(q.getId()).getSelectedAnswer())
                            .question(q.getQuestion())
                            .audioUrl(q.getAudioUrl())
                            .explanation(q.getExplaination())
                            .build());
                }
            }
        }
        examHistoryResponse.setQuestions(questions);
        return examHistoryResponse;
    }
}
