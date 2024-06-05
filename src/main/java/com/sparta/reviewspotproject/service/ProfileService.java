package com.sparta.reviewspotproject.service;

import com.sparta.reviewspotproject.dto.ProfileRequestDto;
import com.sparta.reviewspotproject.dto.ProfileResponseDto;
import com.sparta.reviewspotproject.entity.User;
import com.sparta.reviewspotproject.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 사용자 프로필 조회
    public ProfileResponseDto getProfile(Long userId) {
        User user = findById(userId);
//        return new ProfileResponseDto(user.getUserName(),user.getEmail(),user.getTagLine());
        return null;
    }

    // 사용자 비밀번호 확인 (본인확인)
    public void checkPassword(Long userId, ProfileRequestDto requestDto) {
        User user = findById(userId);
        String inputPassword = passwordEncoder.encode(requestDto.getPassword());
        if (!passwordEncoder.matches(inputPassword, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }

    // 사용자 프로필 수정
    @Transactional
    public void updateProfile(Long userId, ProfileRequestDto requestDto) {
        User user = findById(userId);
        String changePassword = requestDto.getChangePassword();

        // 변경하려는 비밀번호와 현재 비밀번호 확인
        if (passwordEncoder.matches(changePassword, user.getPassword())) {
            throw new IllegalArgumentException("동일한 패스워드로 변경은 불가능합니다.");
        }
        user.update(requestDto);
    }


    private User findById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new IllegalArgumentException("해당 사용자는 존재하지 않습니다.")
        );
    }
}