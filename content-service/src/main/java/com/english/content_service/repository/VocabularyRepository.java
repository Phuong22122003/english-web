package com.english.content_service.repository;

import com.english.content_service.entity.Vocabulary;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VocabularyRepository extends JpaRepository<Vocabulary, String> {
    // Add custom query methods here if needed
    public Page<Vocabulary> findByTopicId(String topicId, Pageable pageable);
}
