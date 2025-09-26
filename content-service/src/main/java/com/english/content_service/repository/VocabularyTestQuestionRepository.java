package com.english.content_service.repository;

import com.english.content_service.entity.VocabularyTestQuestion;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VocabularyTestQuestionRepository extends JpaRepository<VocabularyTestQuestion, String> {
    // Add custom query methods here if needed
    public List<VocabularyTestQuestion> findByTestId(String testId);
}
