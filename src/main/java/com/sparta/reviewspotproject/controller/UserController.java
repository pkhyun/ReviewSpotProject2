package com.sparta.reviewspotproject.controller;

import com.sparta.reviewspotproject.dto.LoginRequestDto;
import com.sparta.reviewspotproject.dto.SignupRequestDto;
import com.sparta.reviewspotproject.entity.User;
import com.sparta.reviewspotproject.security.UserDetailsImpl;
import com.sparta.reviewspotproject.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    // 회원 가입
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody @Valid SignupRequestDto requestDto, BindingResult bindingResult) {
        // Validation 예외처리
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if(fieldErrors.size() > 0) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                log.error(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage());
            }
            return new ResponseEntity<>("잘못된 요청입니다.", HttpStatus.BAD_REQUEST);
        }
        userService.signup(requestDto);

        return ResponseEntity.ok("회원가입이 성공적으로 완료되었습니다.");
    }


    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request , @AuthenticationPrincipal UserDetailsImpl userDetails) {
        // 현재 인증된 사용자 세션 무효화
        SecurityContextHolder.clearContext();
        userService.setNullRefreshToken(request);
        return ResponseEntity.ok("로그아웃 되었습니다.");
    }



    @ExceptionHandler // 에러 핸들링
    private ResponseEntity<String> handleException(IllegalArgumentException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

}