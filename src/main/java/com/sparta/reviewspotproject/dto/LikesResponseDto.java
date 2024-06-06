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

    public LikesResponseDto(Long postId, Integer likesCount) {
        this.postId = postId;
        this.likesCount = likesCount;
    }
}
