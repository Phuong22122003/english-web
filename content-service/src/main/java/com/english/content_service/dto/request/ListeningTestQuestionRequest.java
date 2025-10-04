package com.english.content_service.dto.request;

import com.english.content_service.entity.Options;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ListeningTestQuestionRequest {
    private String question;
    private Options options;
    private String correctAnswer;
    private String explaination;
}
