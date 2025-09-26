package com.english.content_service.repository;

import com.english.content_service.entity.VocabularyTest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VocabularyTestRepository extends JpaRepository<VocabularyTest, String> {
    // Add custom query methods here if needed
    Page<VocabularyTest> findByTopicId(String topicId, Pageable pageable);
}
