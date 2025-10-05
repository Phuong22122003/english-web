package com.english.dto.response;

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
public class VocabularyResponse {
    private String id;
    private String topicId;
    private String word;
    private String phonetic;
    private String meaning;
    private String example;
    private String exampleMeaning;
    private String audioUrl;
    private String imageUrl;
    private String createdAt;
}
