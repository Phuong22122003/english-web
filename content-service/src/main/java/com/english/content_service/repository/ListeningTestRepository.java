package com.english.content_service.repository;

import com.english.content_service.entity.ListeningTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ListeningTestRepository extends JpaRepository<ListeningTest, String> {
    // Add custom query methods here if needed
    Page<ListeningTest> findTestsByTopicId(String topicId, Pageable page);

    @Query("FROM ListeningTest t where t.topic.id=:topicId")
    List<ListeningTest> findAllByTopicId(String topicId);
}
