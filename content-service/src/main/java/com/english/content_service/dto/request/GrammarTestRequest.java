package com.english.content_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GrammarTestRequest {
    private String name;
    private Integer duration;
    private List<GrammarTestQuestionRequest> questions;
}
