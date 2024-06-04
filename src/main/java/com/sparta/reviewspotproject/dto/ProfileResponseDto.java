package com.sparta.reviewspotproject.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProfileResponseDto {
    private String userId;
    private String username;
    private String email;
    private String tagline;

        public ProfileResponseDto(User user) {
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.tagline = user.getTagline();
    }

    public ProfileResponseDto(User user) {
        this.userId = user.getUserId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.tagline = user.getTagline();
    }


}