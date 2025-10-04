package com.english.content_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VocabularyRequest {
    private String word;
    private String phonetic;
    private String meaning;
    private String example;
    private String exampleMeaning;
}
