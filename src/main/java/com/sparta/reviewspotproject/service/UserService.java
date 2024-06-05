package com.sparta.reviewspotproject.service;

import com.sparta.reviewspotproject.dto.LoginRequestDto;
import com.sparta.reviewspotproject.dto.SignupRequestDto;
import com.sparta.reviewspotproject.entity.User;
import com.sparta.reviewspotproject.jwt.JwtUtil;
import com.sparta.reviewspotproject.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // 회원가입
    public void signup(SignupRequestDto requestDto) {
        String userId = requestDto.getUserId();
        String password = passwordEncoder.encode(requestDto.getPassword());
        String userName = requestDto.getUserName();

        // 회원 중복 확인
        Optional<User> checkUsername = userRepository.findByUserId(userId);
        if (checkUsername.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        // email 중복확인
        String email = requestDto.getEmail();
        Optional<User> checkEmail = userRepository.findByEmail(email);
        if (checkEmail.isPresent()) {
            throw new IllegalArgumentException("중복된 Email 입니다.");
        }

        // 사용자 등록
        User user = new User(userId, password, userName, email);
        userRepository.save(user);
    }

    public void setNullRefreshToken(HttpServletRequest request) {
        String tokenValue = jwtUtil.getJwtFromHeader(request);
        Claims claims = jwtUtil.getUserInfoFromToken(tokenValue);
        String userId = claims.getSubject();
        User user = userRepository.findByUserId(userId).orElseThrow(()->
                new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        user.setRefreshToken(null);
        userRepository.save(user);
    }




    public void login(LoginRequestDto requestDto) {
    }
}