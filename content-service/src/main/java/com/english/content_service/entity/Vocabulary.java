package com.english.content_service.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "vocabulary")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vocabulary {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "topic_id")
    private VocabularyTopic topic;

    private String word;

    private String phonetic;

    private String meaning;

    private String example;

    @Column(name = "example_meaning")
    private String exampleMeaning;

    @Column(name = "audio_url")
    private String audioUrl;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "public_audio_id")
    private String publicAudioId;

    @Column(name = "public_image_id")
    private String publicImageId;

}