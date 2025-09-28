package com.english.content_service.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "vocabulary_test")
public class VocabularyTest {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "topic_id")
    private VocabularyTopic topic;

    private String name;

    private int duration;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}