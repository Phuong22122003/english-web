package com.english.content_service.repository;

import com.english.content_service.entity.Grammar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GrammarRepository extends JpaRepository<Grammar, String> {
    // Add custom query methods here if needed
    public List<Grammar> findByTopicId(String topicId);
}
