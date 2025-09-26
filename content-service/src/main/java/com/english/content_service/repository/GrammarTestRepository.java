package com.english.content_service.repository;

import com.english.content_service.entity.GrammarTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GrammarTestRepository extends JpaRepository<GrammarTest, String> {
    // Add custom query methods here if needed
}
