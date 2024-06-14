package com.sparta.reviewspotproject.entity;

import com.sparta.reviewspotproject.dto.ProfileRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class UserTest {


    @Test
    @DisplayName("userEntity 생성 테스트")
    void createUserEntity() {
        // Given
        String userId = "testUser";
        String password = "password";
        String userName = "Test User";
        String email = "test@gmail.com";
        UserStatus userStatus = UserStatus.MEMBER;

        // When
        User testUser = new User(userId, password, userName, email, userStatus);

        // Then
        assertNotNull(testUser);
        assertEquals(userId, testUser.getUserId());
        assertEquals(password, testUser.getPassword());
        assertEquals(userName, testUser.getUserName());
        assertEquals(email, testUser.getEmail());
        assertEquals(userStatus, testUser.getUserStatus());
        assertNotNull(testUser.getCreatedAt());
        assertNotNull(testUser.getModifiedAt());
    }

    @Test
    @DisplayName("User 엔티티 프로필 업데이트 테스트")
    void updateUserProfile() {
        // Given
        User user = new User("testUser", "password", "Test User", "test@example.com", UserStatus.MEMBER);
        ProfileRequestDto requestDto = new ProfileRequestDto();
        requestDto.setUserName("Updated User");
        requestDto.setTagLine("한 줄 소개");

        // When
        user.update(requestDto);

        // Then
        assertEquals("Updated User", user.getUserName());
        assertEquals("한 줄 소개", user.getTagLine());
    }

}