package com.sparta.reviewspotproject.controller;

import com.sparta.reviewspotproject.dto.ProfileRequestDto;
import com.sparta.reviewspotproject.dto.ProfileResponseDto;
import com.sparta.reviewspotproject.entity.User;
import com.sparta.reviewspotproject.exceptionHandler.Message;
import com.sparta.reviewspotproject.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    // 사용자의 프로필 조회
    @GetMapping("/{userId}/profile")
    public ResponseEntity<ProfileResponseDto> getProfile(@PathVariable Long userId) {
        ProfileResponseDto response = profileService.getProfile(userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 사용자의 비밀번호 확인(본인확인)
    @PostMapping("{userId}/profile")
    public ResponseEntity<Message> checkPassword(@PathVariable Long userId, @RequestBody @Valid ProfileRequestDto profileRequestDto) {
        return new ResponseEntity<>(new Message("사용자 확인이 완료되었습니다.","200"), HttpStatus.OK);
    }

    // 사용자의 프로필 수정
    @PutMapping("/{userId}/profile")
    public ResponseEntity<Message> updateProfile(@PathVariable Long userId, @RequestBody @Valid ProfileRequestDto requestDto) {
        profileService.updateProfile(userId, requestDto);
        return new ResponseEntity<>(new Message("성공적으로 수정되었습니다.","200"), HttpStatus.OK);
    }

}