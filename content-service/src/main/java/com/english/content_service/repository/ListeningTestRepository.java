package com.english.content_service.repository;

import com.english.content_service.entity.ListeningTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ListeningTestRepository extends JpaRepository<ListeningTest, String> {
    // Add custom query methods here if needed
}
