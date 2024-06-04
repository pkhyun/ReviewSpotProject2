package com.sparta.reviewspotproject.controller;

import com.sparta.reviewspotproject.dto.ProfileRequestDto;
import com.sparta.reviewspotproject.response.Message;
import com.sparta.reviewspotproject.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

//    @PutMapping("/{user-id}/profile")
//    public ResponseEntity<Message> updateProfile(@RequestBody @Valid ProfileRequestDto requestDto, @AuthenticationPrincipal User user) {
//        profileService.update(requestDto, user);
//        Message message = new Message("성공적으로 수정되었습니다.",HttpStatus.OK);
//        return new ResponseEntity<>(message, HttpStatus.OK);
//    }

}