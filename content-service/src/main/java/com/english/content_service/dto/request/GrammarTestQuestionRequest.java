package com.english.content_service.dto.request;

import com.english.content_service.entity.Options;

public class GrammarTestQuestionRequest {
    private String question;

    private Options options;

    private String correctAnswer;

    private Integer questionOrder;
}
