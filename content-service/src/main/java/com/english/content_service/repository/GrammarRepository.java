package com.english.content_service.repository;

import com.english.content_service.entity.Grammar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GrammarRepository extends JpaRepository<Grammar, String> {
    // Add custom query methods here if needed
}
