package com.english.content_service.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GrammarTestQuestionResponse {
    private String id;
    private String testId; // lấy từ GrammarTest
    private String question;
    private String options;       // JSONB -> String
    private String correctAnswer;
    private Integer questionOrder;
}
