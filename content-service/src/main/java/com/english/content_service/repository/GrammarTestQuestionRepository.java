package com.english.content_service.repository;

import com.english.content_service.entity.GrammarTestQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GrammarTestQuestionRepository extends JpaRepository<GrammarTestQuestion, String> {
    // Add custom query methods here if needed
}
