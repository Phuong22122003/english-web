package com.english.learning_service.dto.response;

import com.english.dto.response.ListeningTestQuestionResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ListeningTestReponse {
    private String id;
    private String name;
    private Integer duration;
    private LocalDateTime createdAt;
    private List<ListeningTestQuestionResponse> questions;
}
