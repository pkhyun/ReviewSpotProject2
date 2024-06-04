package com.sparta.reviewspotproject.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ProfileResponseDto {
    private String username;
    private String email;
    private String tagline;

    public ProfileResponseDto(String username, String email, String tagline) {
        this.username = username;
        this.email = email;
        this.tagline = tagline;
    }
}