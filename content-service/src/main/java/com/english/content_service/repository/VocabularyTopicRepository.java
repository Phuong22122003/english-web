package com.english.content_service.repository;

import com.english.content_service.entity.VocabularyTopic;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface VocabularyTopicRepository extends JpaRepository<VocabularyTopic, String> {
    // Add custom query methods here if needed
    public Page<VocabularyTopic> findAll(Pageable pageable);
}
