package com.english.content_service.service.implt;

import com.english.content_service.dto.response.GetGrammarTopicResponse;
import com.english.content_service.dto.response.GrammarTestQuestionResponse;
import com.english.content_service.dto.response.GrammarTestResponse;
import com.english.content_service.dto.response.GrammarTopicResponse;
import com.english.content_service.entity.Grammar;
import com.english.content_service.entity.GrammarTest;
import com.english.content_service.entity.GrammarTestQuestion;
import com.english.content_service.entity.GrammarTopic;
import com.english.content_service.mapper.GrammarMapper;
import com.english.content_service.repository.GrammarRepository;
import com.english.content_service.repository.GrammarTestQuestionRepository;
import com.english.content_service.repository.GrammarTestRepository;
import com.english.content_service.repository.GrammarTopicRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import com.english.content_service.service.GrammarService;

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
    public Page<GrammarTestResponse> getTestsByGrammarId(String grammarId, int page, int size) {
        Page<GrammarTest> tests = grammarTestRepository.findByGrammarId(grammarId, PageRequest.of(page, size));
        List<GrammarTestResponse> testResponses = grammarMapper.toGrammarTestResponses(tests.getContent());
        return new PageImpl<>(testResponses, PageRequest.of(page, size), tests.getTotalElements());
    }

    @Override
    public List<GrammarTestQuestionResponse> getTestQuestionsByTestId(String testId) {
        List<GrammarTestQuestion> questions = grammarTestQuestionRepository.findByTestId(testId);
        return grammarMapper.toGrammarTestQuestionResponses(questions);
    }
}
