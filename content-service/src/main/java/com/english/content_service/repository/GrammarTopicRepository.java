package com.english.content_service.repository;

import com.english.content_service.entity.GrammarTopic;
import com.english.content_service.entity.VocabularyTopic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GrammarTopicRepository extends JpaRepository<GrammarTopic, String> {
    // Add custom query methods here if needed
    public Page<GrammarTopic> findAll(Pageable pageable);
    Page<GrammarTopic> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
            String name,
            String description,
            Pageable pageable
    );
}
