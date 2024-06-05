package com.sparta.reviewspotproject.repository;

import com.sparta.reviewspotproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserId(String username);
    Optional<User> findByEmail(String email);
}
