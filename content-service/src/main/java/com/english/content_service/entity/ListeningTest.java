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
@Table(name = "listening_test")
public class ListeningTest {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "topic_id")
    private ListeningTopic topic;

    private String name;

    private Integer duration;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}