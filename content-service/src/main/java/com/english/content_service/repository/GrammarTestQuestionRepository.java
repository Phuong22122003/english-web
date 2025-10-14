package com.english.content_service.repository;

import com.english.content_service.entity.GrammarTestQuestion;
import com.english.content_service.entity.VocabularyTestQuestion;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GrammarTestQuestionRepository extends JpaRepository<GrammarTestQuestion, String> {
    // Add custom query methods here if needed
    public List<GrammarTestQuestion> findByTestId(String testId);

    @Modifying
    @Query("DELETE FROM GrammarTestQuestion q WHERE q.test.id = :testId")
    void deleteByTestId(@Param("testId") String testId);

    @Modifying
    @Query("DELETE FROM GrammarTestQuestion q WHERE q.test.id IN :testIds")
    void deleteByTestIds(@Param("testIds") List<String> testIds);

}
