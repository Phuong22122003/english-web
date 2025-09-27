package com.english.content_service.repository;

import com.english.content_service.entity.GrammarTestQuestion;
import com.english.content_service.entity.VocabularyTestQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GrammarTestQuestionRepository extends JpaRepository<GrammarTestQuestion, String> {
    // Add custom query methods here if needed
    public List<GrammarTestQuestion> findByTestId(String testId);
}
