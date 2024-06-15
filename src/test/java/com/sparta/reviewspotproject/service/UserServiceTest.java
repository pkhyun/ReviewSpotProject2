package com.sparta.reviewspotproject.service;

import com.sparta.reviewspotproject.dto.SignupRequestDto;
import com.sparta.reviewspotproject.entity.User;
import com.sparta.reviewspotproject.entity.UserStatus;
import com.sparta.reviewspotproject.repository.UserRepository;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        // Mocking behavior for JavaMailSender
        when(javaMailSender.createMimeMessage()).thenReturn(mock(MimeMessage.class));
    }

    @Test
    @DisplayName("회원 가입 테스트")
    public void signupTest() {
        // Given
        SignupRequestDto signupRequestDto = Mockito.mock(SignupRequestDto.class);
        when(signupRequestDto.getUserId()).thenReturn("User123Name");
        when(signupRequestDto.getPassword()).thenReturn("Password123!@");
        when(signupRequestDto.getUserName()).thenReturn("박강현");
        when(signupRequestDto.getEmail()).thenReturn("testuser@gmail.com");

        UserStatus userStatus = UserStatus.MEMBER;

        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        // 중복되지 않은 email
        when(userRepository.findByEmail("testuser@gmail.com")).thenReturn(Optional.empty());
        // 중복되지 않은 아이디
        when(userRepository.findByUserId("User123Name")).thenReturn(Optional.empty());

        // When
        userService.signup(signupRequestDto);

        // Then

        ArgumentCaptor<User> userCaptor = forClass(User.class);
        verify(userRepository, times(1)).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();

        assertEquals("User123Name", savedUser.getUserId());
        assertEquals("encodedPassword", savedUser.getPassword());
        assertEquals("박강현", savedUser.getUserName());
        assertEquals("testuser@gmail.com", savedUser.getEmail());
        assertEquals(UserStatus.MEMBER, savedUser.getUserStatus());
    }

    @Test
    @DisplayName("회원 탈퇴 테스트")
    public void setUserStatusTest() {
        // Given
        User user = new User("User123Name", "encodedPassword", "박강현", "testuser@gmail.com", UserStatus.MEMBER);

        // When
        userService.setUserStatus(user);

        // Then
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(userCaptor.capture());
        User updatedUser = userCaptor.getValue();

        assertEquals(UserStatus.NON_MEMBER, updatedUser.getUserStatus());
    }

    @Test
    @DisplayName("로그아웃 테스트")
    public void setNullRefreshTokenTest() {
        // Given
        User user = new User("User123Name", "encodedPassword", "박강현", "testuser@gmail.com", UserStatus.MEMBER);
        user.setRefreshToken("someRefreshToken");

        // When
        userService.setNullRefreshToken(user);

        // Then
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(userCaptor.capture());
        User updatedUser = userCaptor.getValue();

        assertNull(updatedUser.getRefreshToken());
    }

}
