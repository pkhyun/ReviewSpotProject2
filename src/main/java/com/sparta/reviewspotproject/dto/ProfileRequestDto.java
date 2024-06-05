package com.sparta.reviewspotproject.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProfileRequestDto {

    @NotBlank
    private String userName;
    @NotBlank
    private String email;
    @NotBlank
    private String tagline;
    // 회원가입시 설정된 비밀번호 형식 (정규식)필요 +메세지 valid 예외처리
    private String password;
    // 비밀번호 형식 (정규식)필요 +메세지 valid 예외처리
    private String changePassword;

}