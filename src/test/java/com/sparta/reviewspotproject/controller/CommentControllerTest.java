package com.sparta.reviewspotproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.reviewspotproject.config.WebSecurityConfig;
import com.sparta.reviewspotproject.dto.CommentRequestDto;
import com.sparta.reviewspotproject.dto.CommentResponseDto;
import com.sparta.reviewspotproject.dto.PostRequestDto;
import com.sparta.reviewspotproject.dto.PostResponseDto;
import com.sparta.reviewspotproject.entity.User;
import com.sparta.reviewspotproject.entity.UserStatus;
import com.sparta.reviewspotproject.security.UserDetailsImpl;
import com.sparta.reviewspotproject.service.CommentService;
import com.sparta.reviewspotproject.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = {CommentController.class},
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = WebSecurityConfig.class
                )
        }
)
class CommentControllerTest {
    private MockMvc mvc;

    private Principal mockPrincipal;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    CommentService commentService;
    @Autowired
    private PostService postService;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity(new MockSpringSecurityFilter()))
                .build();
    }

    private void mockUserSetup() {
        // Mock 테스트 유져 생성
        String userId = "testUser";
        String password = "password";
        String userName = "Test User";
        String email = "test@gmail.com";
        UserStatus userStatus = UserStatus.MEMBER;
        User testUser = new User(userId, password, userName, email, userStatus);
        UserDetailsImpl testUserDetails = new UserDetailsImpl(testUser);
        mockPrincipal = new UsernamePasswordAuthenticationToken(testUserDetails, "", testUserDetails.getAuthorities());
    }

    @Test
    @DisplayName("댓글 생성 테스트")
    public void testCreateComment() throws Exception {
        // Given
        Long commentId = 1L;
        this.mockUserSetup();
        CommentRequestDto requestDto = Mockito.mock(CommentRequestDto.class);
        when(requestDto.getContents()).thenReturn("테스트 내용");

        String requestBody = objectMapper.writeValueAsString(requestDto);

        // When & Then
        mvc.perform(post("/api/comment/" + commentId)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("댓글 수정 테스트")
    public void updateCommentTest() throws Exception {
        // Given
        Long commentId = 1L;
        this.mockUserSetup();
        CommentRequestDto requestDto = Mockito.mock(CommentRequestDto.class);
        when(requestDto.getContents()).thenReturn("수정 내용");

        String requestBody = objectMapper.writeValueAsString(requestDto);

        // When & Then
        mvc.perform(put("/api/comment/" + commentId)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("댓글 삭제 테스트")
    public void deleteCommentTest() throws Exception {
        // Given
        Long commentId = 1L;
        this.mockUserSetup();

        // When & Then
        mvc.perform(delete("/api/comment/" + commentId)
                        .principal(mockPrincipal)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("선택한 게시물의 댓글들 조회 테스트")
    public void getAllCommentTest() throws Exception {
        // Given
        Long postId = 1L;

        CommentResponseDto commentResponseDto1 = Mockito.mock(CommentResponseDto.class);
        when(commentResponseDto1.getId()).thenReturn(1L);
        when(commentResponseDto1.getContents()).thenReturn("테스트 내용1");
        when(commentResponseDto1.getCreatedAt()).thenReturn(LocalDateTime.now());
        when(commentResponseDto1.getModifiedAt()).thenReturn(LocalDateTime.now());
        when(commentResponseDto1.getCommentLikesCount()).thenReturn(5);

        CommentResponseDto commentResponseDto2 = Mockito.mock(CommentResponseDto.class);
        when(commentResponseDto2.getId()).thenReturn(2L);
        when(commentResponseDto2.getContents()).thenReturn("테스트 내용2");
        when(commentResponseDto2.getCreatedAt()).thenReturn(LocalDateTime.now());
        when(commentResponseDto2.getModifiedAt()).thenReturn(LocalDateTime.now());
        when(commentResponseDto2.getCommentLikesCount()).thenReturn(10);

        List<CommentResponseDto> comments = Arrays.asList(commentResponseDto1, commentResponseDto2);
        when(commentService.getAllComment(postId)).thenReturn(comments);

        // When & Then
        mvc.perform(get("/api/comments/" + postId)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

}