package com.english.learning_service.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetVocabularyTestQuestionResponse {
    private String topicId;
    private String topicName;
    private Integer duration;
    private String testId;
    private String testName;
    private List<VocabularyTestQuestionResponse> questions;
}
