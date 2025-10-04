package com.english.content_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListeningTestRequest {
    private String name;
    private Integer duration;
    private List<ListeningTestQuestionRequest> questions;
}
