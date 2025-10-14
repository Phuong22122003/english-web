package com.english.content_service.repository;

import com.english.content_service.entity.Grammar;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GrammarRepository extends JpaRepository<Grammar, String> {
    // Add custom query methods here if needed
    public List<Grammar> findByTopicId(String topicId);

    @Modifying
    @Query("DELETE FROM Grammar g WHERE g.topic.id = :topicId")
    void deleteByTopicId(@Param("topicId") String topicId);
}
