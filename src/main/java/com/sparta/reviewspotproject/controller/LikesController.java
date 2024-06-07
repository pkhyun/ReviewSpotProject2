package com.sparta.reviewspotproject.controller;

import com.sparta.reviewspotproject.dto.LikesResponseDto;
import com.sparta.reviewspotproject.security.UserDetailsImpl;
import com.sparta.reviewspotproject.service.LikesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LikesController {

    private final LikesService likesService;

    // 게시물의 좋아요
    @PostMapping("/post/{postId}/likes")
    public ResponseEntity<LikesResponseDto> likePost(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Integer likesCount = likesService.likePost(postId,userDetails.getUser());
        LikesResponseDto responseDto = new LikesResponseDto(postId,likesCount,"게시물에 좋아요를 등록했습니다.");
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // 게시물의 좋아요 취소
    @DeleteMapping("/post/{postId}/unlikes")
    public ResponseEntity<LikesResponseDto> unlikePost(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Integer likesCount = likesService.unlikePost(postId,userDetails.getUser());
        LikesResponseDto responseDto = new LikesResponseDto(postId,likesCount,"게시물에 좋아요를 취소했습니다.");
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

}
