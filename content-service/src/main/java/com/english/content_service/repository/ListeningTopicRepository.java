package com.english.content_service.repository;

import com.english.content_service.entity.ListeningTopic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ListeningTopicRepository extends JpaRepository<ListeningTopic, String> {
    // Add custom query methods here if needed
}
