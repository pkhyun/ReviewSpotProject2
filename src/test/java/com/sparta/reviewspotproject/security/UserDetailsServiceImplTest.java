package com.sparta.reviewspotproject.security;

import com.sparta.reviewspotproject.entity.User;
import com.sparta.reviewspotproject.entity.UserStatus;
import com.sparta.reviewspotproject.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsServiceImpl;

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User("User123Name", "encodedPassword", "박강현", "testuser@gmail.com", UserStatus.MEMBER);
        user.setRefreshToken("RefreshToken");
    }

    @Test
    @DisplayName("유저 리프레시 토큰 저장 테스트")
    public void updateRefreshTokenTest() {
        // Given
        Mockito.when(userRepository.findByUserId("User123Name")).thenReturn(Optional.of(user));

        // When
        userDetailsServiceImpl.updateRefreshToken("User123Name", "newRefreshToken");

        // Then
        Mockito.verify(userRepository, Mockito.times(1)).save(user);
        assertEquals("newRefreshToken", user.getRefreshToken());
    }

    @Test
    @DisplayName("유저 찾기 테스트")
    public void findByUserIdTest() {
        // Given
        Mockito.when(userRepository.findByUserId("User123Name")).thenReturn(Optional.of(user));

        // When
        User foundUser = userDetailsServiceImpl.findByUserId("User123Name");

        // Then
        assertEquals(user, foundUser);
    }

    @Test
    @DisplayName("유저 리프레시 토큰 가져오기 테스트")
    public void getRefreshTokenTest() {
        // Given
        Mockito.when(userRepository.findByUserId("User123Name")).thenReturn(Optional.of(user));

        // When
        String refreshToken = userDetailsServiceImpl.getRefreshToken("User123Name");

        // Then
        assertEquals("initialRefreshToken", refreshToken);
    }

    @Test
    @DisplayName("유저 이름으로 찾기 테스트")
    public void loadUserByUsernameTest() {
        // Given
        Mockito.when(userRepository.findByUserId("User123Name")).thenReturn(Optional.of(user));

        // When
        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsServiceImpl.loadUserByUsername("User123Name");

        // Then
        assertEquals(user, userDetails.getUser());
    }

    @Test
    @DisplayName("유저 이름으로 찾기 실패 테스트")
    public void loadUserByUsernameNotFoundTest() {
        // Given
        Mockito.when(userRepository.findByUserId("UnknownUser")).thenReturn(Optional.empty());

        // When / Then
        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsServiceImpl.loadUserByUsername("UnknownUser");
        });
    }
}
