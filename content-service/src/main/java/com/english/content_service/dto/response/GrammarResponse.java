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
public class GrammarResponse {
    private String id;
    private String topicId; // lấy từ GrammarTopic
    private String title;
    private String content;
    private String createdAt;
}
