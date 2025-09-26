package com.english.content_service.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "listening")
public class Listening {
    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "topic_id")
    private ListeningTopic topic;

    private String name;

    @Column(name = "audio_url")
    private String audioUrl;

    @Column(name = "image_url")
    private String imageUrl;

    private String transcript;

    private String question;

    @Column(columnDefinition = "jsonb")
    private String options;

    @Column(name = "correct_answer")
    private String correctAnswer;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}