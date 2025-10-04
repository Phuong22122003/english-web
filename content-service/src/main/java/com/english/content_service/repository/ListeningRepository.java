package com.english.content_service.repository;

import com.english.content_service.entity.Listening;
import com.english.content_service.entity.ListeningTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ListeningRepository extends JpaRepository<Listening, String> {
    // Add custom query methods here if needed
    List<Listening> findByTopicId(String topicId);
}
