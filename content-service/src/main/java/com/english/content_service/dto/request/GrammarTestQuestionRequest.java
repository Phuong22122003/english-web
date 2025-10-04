package com.english.content_service.dto.request;

import com.english.content_service.entity.Options;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GrammarTestQuestionRequest {
    private String question;

    private Options options;

    private String correctAnswer;

    private Integer questionOrder;

    private String explaination;
}
