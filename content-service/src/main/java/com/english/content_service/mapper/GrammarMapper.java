package com.english.content_service.mapper;

import com.english.content_service.dto.response.GrammarResponse;
import com.english.content_service.dto.response.GrammarTestQuestionResponse;
import com.english.content_service.dto.response.GrammarTestResponse;
import com.english.content_service.dto.response.GrammarTopicResponse;
import com.english.content_service.entity.Grammar;
import com.english.content_service.entity.GrammarTest;
import com.english.content_service.entity.GrammarTestQuestion;
import com.english.content_service.entity.GrammarTopic;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GrammarMapper {

    // Topic
    GrammarTopicResponse toGrammarTopicResponse(GrammarTopic topic);
    List<GrammarTopicResponse> toGrammarTopicResponses(List<GrammarTopic> topics);

    // Grammar
    @Mapping(target = "topicId", source = "topic.id")
    GrammarResponse toGrammarResponse(Grammar grammar);
    List<GrammarResponse> toGrammarResponses(List<Grammar> grammars);

    // Test
    @Mapping(target = "grammarId", source = "grammar.id")
    GrammarTestResponse toGrammarTestResponse(GrammarTest test);
    List<GrammarTestResponse> toGrammarTestResponses(List<GrammarTest> tests);

    // Question
    @Mapping(target = "testId", source = "test.id")
    GrammarTestQuestionResponse toGrammarTestQuestionResponse(GrammarTestQuestion question);
    List<GrammarTestQuestionResponse> toGrammarTestQuestionResponses(List<GrammarTestQuestion> questions);
}
