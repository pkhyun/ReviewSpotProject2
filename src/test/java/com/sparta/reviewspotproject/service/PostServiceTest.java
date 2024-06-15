package com.sparta.reviewspotproject.service;

import com.sparta.reviewspotproject.dto.PostRequestDto;
import com.sparta.reviewspotproject.dto.PostResponseDto;
import com.sparta.reviewspotproject.entity.Post;
import com.sparta.reviewspotproject.entity.User;
import com.sparta.reviewspotproject.entity.UserStatus;
import com.sparta.reviewspotproject.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostService postService;

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User("testUser", "encodedPassword", "박강현", "testuser@gmail.com", UserStatus.MEMBER);
        user.setId(1L);
    }

    @Test
    @DisplayName("게시글 작성 테스트")
    public void createPostTest() {
        // Given
        PostRequestDto requestDto = Mockito.mock(PostRequestDto.class);
        when(requestDto.getTitle()).thenReturn("테스트 제목");
        when(requestDto.getContents()).thenReturn("테스트 내용");

        // When
        PostResponseDto responseDto = postService.createPost(requestDto, user);

        // Then
        assertEquals(requestDto.getTitle(), responseDto.getTitle());
        assertEquals(requestDto.getContents(), responseDto.getContents());
    }

    @Test
    @DisplayName("게시글 조회 테스트")
    public void getPostTest() {
        // Given
        Post post = Mockito.mock(Post.class);
        when(post.getId()).thenReturn(1L);
        when(post.getTitle()).thenReturn("테스트 제목");
        when(post.getContents()).thenReturn("테스트 내용");
        when(post.getCreatedAt()).thenReturn(LocalDateTime.now());
        when(post.getModifiedAt()).thenReturn(LocalDateTime.now());
        when(post.getPostLikesCount()).thenReturn(5);
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        // When
        PostResponseDto responseDto = postService.getPost(1L);

        // Then
        assertEquals(post.getTitle(), responseDto.getTitle());
        assertEquals(post.getContents(), responseDto.getContents());
        assertEquals(post.getCreatedAt(), responseDto.getCreateAt());
        assertEquals(post.getModifiedAt(), responseDto.getModifiedAt());
        assertEquals(post.getPostLikesCount(), responseDto.getPostLikesCount());
    }

    @Test
    @DisplayName("게시글 수정 테스트")
    public void updatePostTest() {
        // Given
        Long postId = 1L;
        PostRequestDto requestDto = Mockito.mock(PostRequestDto.class);
        when(requestDto.getTitle()).thenReturn("수정 제목");
        when(requestDto.getContents()).thenReturn("수정 내용");
        Post post = Mockito.mock(Post.class);
        when(post.getId()).thenReturn(1L);
        when(post.getTitle()).thenReturn("테스트 제목");
        when(post.getContents()).thenReturn("테스트 내용");
        when(post.getCreatedAt()).thenReturn(LocalDateTime.now());
        when(post.getModifiedAt()).thenReturn(LocalDateTime.now());
        when(post.getPostLikesCount()).thenReturn(5);
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        // When
        PostResponseDto responseDto = postService.update(postId, requestDto, user);

        // Then
        assertEquals(requestDto.getTitle(), responseDto.getTitle());
        assertEquals(requestDto.getContents(), responseDto.getContents());
    }
}
