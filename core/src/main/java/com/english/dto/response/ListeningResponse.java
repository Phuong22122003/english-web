package com.english.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListeningResponse {
    private String id;

    private ListeningTopicResponse topic;

    private String name;

    private String audioUrl;

    private String imageUrl;

    private String transcript;

    private String question;

    private Options options;

    private String correctAnswer;

    private LocalDateTime createdAt;
}
