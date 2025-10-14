package com.english.content_service.repository;

import com.english.content_service.entity.GrammarTest;
import com.english.content_service.entity.VocabularyTest;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GrammarTestRepository extends JpaRepository<GrammarTest, String> {
    // Add custom query methods here if needed
    Page<GrammarTest> findByGrammarId(String topicId, Pageable pageable);

    List<GrammarTest> findByGrammarId(String grammarId);

    @Modifying
    @Query("DELETE FROM GrammarTest t WHERE t.grammar.id = :grammarId")
    void deleteByGrammarId(@Param("grammarId") String grammarId);

    @Modifying
    @Query("DELETE FROM GrammarTest t WHERE t.grammar.id IN :grammarIds")
    void deleteByGrammarIds(@Param("grammarIds") List<String> grammarIds);
}
