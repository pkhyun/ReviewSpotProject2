package com.sparta.reviewspotproject.service;

import com.sparta.reviewspotproject.entity.Likes;
import com.sparta.reviewspotproject.entity.Post;
import com.sparta.reviewspotproject.entity.User;
import com.sparta.reviewspotproject.repository.LikesRepository;
import com.sparta.reviewspotproject.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikesService {

    private final LikesRepository likesRepository;
    private final PostRepository postRepository;

    @Transactional
    public Integer likePost(Long postId, User user) {
        Post post = findPostId(postId);

        // 사용자가 게시물의 작성자인지 확인
        if (post.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("자신의 게시물에 좋아요를 할 수 없습니다.");
        }
        // 사용자가 이미 좋아요를 했는지 확인
        if (likesRepository.findByPostIdAndUserId(postId, user.getId()).isPresent()) {
            throw new IllegalArgumentException("중복 좋아요는 할 수 없습니다.");
        }

        // 사용자가 좋아요를 한 내역이 없는지 확인
        if(!likesRepository.existsByPostAndUser(post, user)) {
            post.setLikesCount(post.getLikesCount() + 1);
            likesRepository.save(new Likes(post,user));
        }
        return post.getLikesCount();
    }

    @Transactional
    public Integer unlikePost(Long postId, User user) {
        Post post = findPostId(postId);

        Likes like = likesRepository.findByPostIdAndUserId(postId, user.getId()).orElseThrow(() ->
                new IllegalArgumentException("해당 게시물에 좋아요를 하지 않았습니다."));

        if(likesRepository.existsByPostAndUser(post, user)) {
            post.setLikesCount(post.getLikesCount() - 1);
        }
        likesRepository.delete(like);
        return post.getLikesCount();
    }

    private Post findPostId(Long postId) {
        return postRepository.findById(postId).orElseThrow(()->
                new IllegalArgumentException("해당 게시물은 존재하지 않습니다."));
    }

}
