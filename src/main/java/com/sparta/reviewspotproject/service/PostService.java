package com.sparta.reviewspotproject.service;

import com.sparta.reviewspotproject.dto.PostRequestDto;
import com.sparta.reviewspotproject.dto.PostResponseDto;
import com.sparta.reviewspotproject.entity.Post;
import com.sparta.reviewspotproject.entity.User;
import com.sparta.reviewspotproject.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    //게시글 작성
    public PostResponseDto createPost(PostRequestDto postRequestDto, User user) {
        Post post = new Post(postRequestDto, user);
        Post savePost = postRepository.save(post);
        return new PostResponseDto(savePost);
    }

    //게시글 조회
    public PostResponseDto getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시물을 찾을 수 없습니다."));
        return new PostResponseDto(post);
    }

    //게시글 전체 조회
    public List<PostResponseDto> getPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc().stream().map(PostResponseDto::new).toList();
    }

    //게시글 수정
    @Transactional
    public PostResponseDto update(Long postId, PostRequestDto postRequestDto, User user) {
        findPost(postId);
        Post post = postRepository.findById(postId).get();
        if (post.getUser().getId() != user.getId()) {
            throw new IllegalArgumentException("게시물 작성자가 아니므로 수정할 수 없습니다.");
        }
        post.update(postRequestDto, user);
        return new PostResponseDto(post);
    }

    //게시글 수정
    public void delete(Long postId, User user) {
        findPost(postId);
        Post post = postRepository.findById(postId).get();
        if (post.getUser().getId() != user.getId()) {
            throw new IllegalArgumentException("게시물 작성자가 아니므로 삭제할 수 없습니다.");
        }
        postRepository.delete(post);
    }

    public Post findPost(Long id){
        return postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시물을 찾을 수 없습니다."));
    }
}