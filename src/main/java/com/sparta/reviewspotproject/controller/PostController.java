package com.sparta.reviewspotproject.controller;

import com.sparta.reviewspotproject.dto.PostRequestDto;
import com.sparta.reviewspotproject.dto.PostResponseDto;
import com.sparta.reviewspotproject.security.UserDetailsImpl;
import com.sparta.reviewspotproject.service.PostService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {

    private final PostService postService;

    //게시글 작성
    @PostMapping("/posts")
    public PostResponseDto createPost(@Valid @RequestBody PostRequestDto postRequestDto,
                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.createPost(postRequestDto, userDetails.getUser());

    }

    //게시글 조회
    @GetMapping("/posts/{postId}")
    public PostResponseDto getPost(@PathVariable Long postId) {
        return postService.getPost(postId);
    }

    //게시글 전체 조회
    @GetMapping("/posts")
    public ResponseEntity<?> getPosts() {
        List<PostResponseDto> posts = postService.getPosts();
        if(posts.isEmpty()){
            return ResponseEntity.ok("먼저 작성하여 소식을 알려보세요!");
        }
        return ResponseEntity.ok(posts);
    }

    //게시글 수정
    @PutMapping("/posts/{postId}")
    public PostResponseDto updatePost(@PathVariable Long postId,
                                      @Valid @RequestBody PostRequestDto postRequestDto,
                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.update(postId, postRequestDto, userDetails.getUser());
    }

    //게시글 삭제
    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable Long postId,
                                             @AuthenticationPrincipal UserDetailsImpl userDetails) {
        postService.delete(postId, userDetails.getUser());
        return ResponseEntity.ok("게시글이 성공적으로 삭제되었습니다.");
    }
}
