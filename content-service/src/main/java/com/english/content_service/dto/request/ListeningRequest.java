package com.english.content_service.dto.request;

import com.english.content_service.entity.ListeningTopic;
import com.english.content_service.entity.Options;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ListeningRequest {
    private String name;
    private String transcript;
    private String question;
    private Options options;
    private String correctAnswer;
}
