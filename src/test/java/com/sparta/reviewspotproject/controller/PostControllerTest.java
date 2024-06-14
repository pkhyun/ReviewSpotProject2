package com.sparta.reviewspotproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.reviewspotproject.config.WebSecurityConfig;
import com.sparta.reviewspotproject.dto.PostRequestDto;
import com.sparta.reviewspotproject.dto.PostResponseDto;
import com.sparta.reviewspotproject.entity.User;
import com.sparta.reviewspotproject.entity.UserStatus;
import com.sparta.reviewspotproject.security.UserDetailsImpl;
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

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = {PostController.class},
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = WebSecurityConfig.class
                )
        }
)
class PostControllerTest {
    private MockMvc mvc;

    private Principal mockPrincipal;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    PostService postService;

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
    @DisplayName("게시물 생성 테스트")
    public void testCreatePost() throws Exception {
        // Given
        this.mockUserSetup();
        PostRequestDto requestDto = Mockito.mock(PostRequestDto.class);
        when(requestDto.getTitle()).thenReturn("테스트 제목");
        when(requestDto.getContents()).thenReturn("테스트 내용");

        String requestBody = objectMapper.writeValueAsString(requestDto);

        // When & Then
        mvc.perform(post("/api/posts")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .principal(mockPrincipal)
        )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("게시물 조회 테스트")
    public void getPostTest() throws Exception {
        // Given
        Long postId = 1L;

        // When & Then
        mvc.perform(get("/api/posts/" + postId)
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("게시물 전체 조회 테스트 - 게시글이 없을 때")
    public void getPostsTest() throws Exception {
        // Given X
        // When & Then
        mvc.perform(get("/api/posts")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("먼저 작성하여 소식을 알려보세요!"))
                .andDo(print());
    }

    @Test
    @DisplayName("게시물 전체 조회 테스트 - 게시글이 있을 때")
    public void getPostsTestSuccess() throws Exception {
        // Given
        PostResponseDto postResponseDto1 = Mockito.mock(PostResponseDto.class);
        when(postResponseDto1.getPostId()).thenReturn(1L);
        when(postResponseDto1.getUserId()).thenReturn(1L);
        when(postResponseDto1.getTitle()).thenReturn("테스트 제목1");
        when(postResponseDto1.getContents()).thenReturn("테스트 내용1");
        when(postResponseDto1.getCreateAt()).thenReturn(LocalDateTime.now());
        when(postResponseDto1.getModifiedAt()).thenReturn(LocalDateTime.now());
        when(postResponseDto1.getPostLikesCount()).thenReturn(5);

        PostResponseDto postResponseDto2 = Mockito.mock(PostResponseDto.class);
        when(postResponseDto2.getPostId()).thenReturn(2L);
        when(postResponseDto2.getUserId()).thenReturn(2L);
        when(postResponseDto2.getTitle()).thenReturn("테스트 제목2");
        when(postResponseDto2.getContents()).thenReturn("테스트 내용2");
        when(postResponseDto2.getCreateAt()).thenReturn(LocalDateTime.now());
        when(postResponseDto2.getModifiedAt()).thenReturn(LocalDateTime.now());
        when(postResponseDto2.getPostLikesCount()).thenReturn(10);

        List<PostResponseDto> posts = Arrays.asList(postResponseDto1, postResponseDto2);
        when(postService.getPosts()).thenReturn(posts);
        // When & Then
        mvc.perform(get("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("게시물 수정 테스트")
    public void updatePostTest() throws Exception {
        // Given
        Long postId = 1L;
        this.mockUserSetup();
        PostRequestDto requestDto = Mockito.mock(PostRequestDto.class);
        when(requestDto.getTitle()).thenReturn("수정 제목");
        when(requestDto.getContents()).thenReturn("수정 내용");

        String requestBody = objectMapper.writeValueAsString(requestDto);

        // When & Then
        mvc.perform(put("/api/posts" + postId)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("게시물 삭제 테스트")
    public void deletePostTest() throws Exception {
        // Given
        Long postId = 1L;
        this.mockUserSetup();

        // When & Then
        mvc.perform(delete("/api/posts" + postId)
                        .principal(mockPrincipal)
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("게시글이 성공적으로 삭제되었습니다."))
                .andDo(print());

    }

}