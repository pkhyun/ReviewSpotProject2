package com.sparta.reviewspotproject.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.reviewspotproject.dto.LoginRequestDto;
import com.sparta.reviewspotproject.jwt.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import java.io.IOException;

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        setFilterProcessesUrl("/api/user/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            LoginRequestDto requestDto = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getUsername(),
                            requestDto.getPassword(),
                            null
                    )
            );
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) {
        String username = ((UserDetailsImpl) authResult.getPrincipal()).getUsername();

        String token = jwtUtil.createToken(username); // 액세스 토큰 생성
        String refreshToken = jwtUtil.createRefreshToken(username); // 리프레시 토큰 생성
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, token); // 액세스 토큰을 헤더에 추가
        // 리프레시 토큰을 쿠키에 저장하여 클라이언트에 전달
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setMaxAge(12 * 60 * 60); // 리프레시 토큰의 유효 기간 설정 (초 단위)
        refreshTokenCookie.setPath("/"); // 쿠키 경로 설정
        response.addCookie(refreshTokenCookie); // 응답에 쿠키 추가
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        log.error("Authentication failed: {}", failed.getMessage());
        response.setStatus(401);
    }

}