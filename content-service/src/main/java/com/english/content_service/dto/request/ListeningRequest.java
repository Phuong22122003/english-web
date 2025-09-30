package com.english.content_service.dto.request;

import com.english.content_service.entity.ListeningTopic;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.LocalDateTime;

public class ListeningRequest {
    private String name;
    private String transcript;
    private String question;
    private String options;
    private String correctAnswer;
}
