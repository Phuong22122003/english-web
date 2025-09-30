package com.english.content_service.dto.response;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListeningTopicResponse {
    private String id;

    private String name;

    private String description;

    private String imageUrl;

    private LocalDateTime createdAt;
}
