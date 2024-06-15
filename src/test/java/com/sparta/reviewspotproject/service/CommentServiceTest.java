package com.sparta.reviewspotproject.service;

import com.sparta.reviewspotproject.dto.CommentRequestDto;
import com.sparta.reviewspotproject.dto.CommentResponseDto;
import com.sparta.reviewspotproject.entity.Comment;
import com.sparta.reviewspotproject.entity.Post;
import com.sparta.reviewspotproject.entity.User;
import com.sparta.reviewspotproject.entity.UserStatus;
import com.sparta.reviewspotproject.repository.CommentRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentService commentService;

    private User user;

    private Post post;

    private Comment comment;

    @BeforeEach
    public void setUp() {
        user = new User("testUser", "encodedPassword", "박강현", "testuser@gmail.com", UserStatus.MEMBER);
        user.setId(1L);

        post = Mockito.mock(Post.class);
        when(post.getId()).thenReturn(1L);
        when(post.getTitle()).thenReturn("테스트 제목");
        when(post.getContents()).thenReturn("테스트 내용");
        when(post.getCreatedAt()).thenReturn(LocalDateTime.now());
        when(post.getModifiedAt()).thenReturn(LocalDateTime.now());
        when(post.getPostLikesCount()).thenReturn(5);

        comment = Mockito.mock(Comment.class);
        when(comment.getId()).thenReturn(1L);
        when(comment.getContents()).thenReturn("테스트 내용");
    }

    @Test
    @DisplayName("댓글 작성 테스트")
    public void createCommentTest() {
        // Given
        CommentRequestDto requestDto = Mockito.mock(CommentRequestDto.class);
        when(requestDto.getContents()).thenReturn("테스트 내용");

        // When
        CommentResponseDto responseDto = commentService.createComment(post.getId(), requestDto, user);

        // Then
        assertEquals(requestDto.getContents(), responseDto.getContents());
    }

    @Test
    @DisplayName("댓글 수정 테스트")
    public void updateCommentTest() {
        // Given
        CommentRequestDto requestDto = Mockito.mock(CommentRequestDto.class);
        when(requestDto.getContents()).thenReturn("수정 내용");

        // when
        CommentResponseDto responseDto = commentService.updateComment(comment.getId(), requestDto, user);

        // Then
        assertEquals(requestDto.getContents(), responseDto.getContents());
    }

    @Test
    @DisplayName("댓글 삭제 테스트")
    public void deleteCommentTest() {
        // Given
        // When
        commentService.deleteComment(comment.getId(), user);

        // Then
        assertNull(commentRepository.findById(comment.getId()));
    }

}