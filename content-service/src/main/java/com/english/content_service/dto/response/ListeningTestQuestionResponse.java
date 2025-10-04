package com.english.content_service.dto.response;

import com.english.content_service.entity.ListeningTest;
import com.english.content_service.entity.Options;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ListeningTestQuestionResponse {
    private String id;
    private ListeningTest test;
    private String audioUrl;
    private String imageUrl;
    private String question;
    private Options options;
    private String correctAnswer;
    private String explaination;
}
