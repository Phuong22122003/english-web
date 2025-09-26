package com.english.content_service.dto.response;

import com.english.content_service.entity.Options;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VocabularyTestQuestionResponse {
    private String id;
    private String testId;
    private String question;
    private Options options;
    private String correctAnswer;
    private Integer questionOrder;
}
