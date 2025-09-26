package com.english.content_service.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.english.content_service.dto.response.VocabTopicResponse;
import com.english.content_service.dto.response.VocabularyResponse;
import com.english.content_service.dto.response.VocabularyTestQuestionResponse;
import com.english.content_service.dto.response.VocabularyTestResponse;
import com.english.content_service.entity.Vocabulary;
import com.english.content_service.entity.VocabularyTest;
import com.english.content_service.entity.VocabularyTestQuestion;
import com.english.content_service.entity.VocabularyTopic;

@Mapper(componentModel = "spring")
public interface VocabularyMapper {
    
    //topic
    List<VocabTopicResponse> toVocabTopicResponses(List<VocabularyTopic> topics);
    


    // vocabulary
    @Mapping(target = "topicId", source = "topic.id")
    VocabularyResponse toVocabularyResponse(Vocabulary vocabulary);
    List<VocabularyResponse> toVocabularyResponses(List<Vocabulary> vocabularies);

    //test
    @Mapping(target = "topicId", source = "topic.id")
    VocabularyTestResponse toVocabularyTestResponse(VocabularyTest test);
    List<VocabularyTestResponse> toVocabularyTestResponses(List<VocabularyTest> tests);

    //question
    @Mapping(target = "testId", source = "test.id")
    VocabularyTestQuestionResponse toVocabularyTestQuestionResponse(VocabularyTestQuestion question);
    List<VocabularyTestQuestionResponse> toVocabularyTestQuestionResponses(List<VocabularyTestQuestion> questions);

}
