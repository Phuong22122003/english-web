package com.english.content_service.repository;

import com.english.content_service.entity.ListeningTestQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ListeningTestQuestionRepository extends JpaRepository<ListeningTestQuestion, String> {
    // Add custom query methods here if needed
}
