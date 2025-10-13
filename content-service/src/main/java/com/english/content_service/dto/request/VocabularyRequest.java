package com.english.content_service.dto.request;

import com.english.enums.RequestType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VocabularyRequest {
    private String id;
    private String word;
    private String phonetic;
    private String meaning;
    private String example;
    private String exampleMeaning;
    private String imageName;
    private String audioName;
    private RequestType action;
}
