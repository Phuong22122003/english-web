package com.english.content_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "grammar_test_question")
public class GrammarTestQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "test_id")
    private GrammarTest test;

    private String question;

    @Column(columnDefinition = "jsonb")
    private String options;

    @Column(name = "correct_answer")
    private String correctAnswer;

    @Column(name = "question_order")
    private Integer questionOrder;
}