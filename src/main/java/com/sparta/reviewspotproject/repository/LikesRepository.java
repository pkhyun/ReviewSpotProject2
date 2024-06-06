package com.sparta.reviewspotproject.repository;

import com.sparta.reviewspotproject.entity.Likes;
import com.sparta.reviewspotproject.entity.Post;
import com.sparta.reviewspotproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes, Long> {

    Optional<Likes> findByPostIdAndUserId(Long postId, Long userId);
    boolean existsByPostAndUser(Post post, User user);
}
