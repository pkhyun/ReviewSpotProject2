package com.sparta.reviewspotproject.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class LikesResponseDto {
    private Long postId;
    private Integer likesCount;
    private String message;

    public LikesResponseDto(Long postId, Integer likesCount, String message) {
        this.postId = postId;
        this.likesCount = likesCount;
        this.message = message;
    }
}
