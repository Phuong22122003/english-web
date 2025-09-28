package com.english.content_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VocabularyTestRequest {
    private String name;
    private int duration;
    private List<VocabularyTestQuestionRequest> questions;
}
