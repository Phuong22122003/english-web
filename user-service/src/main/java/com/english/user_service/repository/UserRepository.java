package com.english.user_service.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.english.user_service.entity.User;

public interface UserRepository extends JpaRepository<User, String> {
    public Optional<User> findByUsername(String username);
    public Optional<User> findByEmail(String username);
    public Boolean existsByUsername(String username);
    public Boolean existsByEmail(String email);

    public List<User> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
