package com.english.content_service.dto.response;

import com.english.content_service.entity.ListeningTopic;
import com.english.content_service.entity.Options;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListeningResponse {
    private String id;

    private ListeningTopic topic;

    private String name;

    private String audioUrl;

    private String imageUrl;

    private String transcript;

    private String question;

    private Options options;

    private String correctAnswer;

    private LocalDateTime createdAt;
}
