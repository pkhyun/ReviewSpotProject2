package com.sparta.reviewspotproject.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProfileRequestDto {

    @NotBlank(message = "이름을 입력해주세요")
    private String userName;

    @NotBlank(message = "Email을 입력해주세요.")
    private String email;

    @NotBlank(message = "내용을 입력해주세요.")
    private String tagLine;

    @NotBlank(message = "패스워드를 입력해주세요.")
    @Pattern(
            regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{10,}$",
            message = "비밀번호는 대소문자 영문, 숫자, 특수문자를 최소 1글자씩 포함하며 최소 10자 이상이어야 합니다."
    )
    private String password;

    @NotBlank(message = "변경하려는 패스워드를 입력해주세요.")
    @Pattern(
            regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{10,}$",
            message = "비밀번호는 대소문자 영문, 숫자, 특수문자를 최소 1글자씩 포함하며 최소 10자 이상이어야 합니다."
    )
    private String changePassword;

}