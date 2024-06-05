package com.sparta.reviewspotproject.service;

import com.sparta.reviewspotproject.dto.ProfileRequestDto;
import com.sparta.reviewspotproject.dto.ProfileResponseDto;
import com.sparta.reviewspotproject.entity.User;
import com.sparta.reviewspotproject.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 사용자 프로필 조회
    public ProfileResponseDto getProfile(Long id) {
        User user = findById(id);
        ProfileResponseDto responseDto = new ProfileResponseDto(user);
        return responseDto;
    }

    // 사용자 프로필 수정
    @Transactional
    public void updateProfile(ProfileRequestDto requestDto, User user) {
        User currentUser = findById(user.getId());
        String password = requestDto.getPassword();
        String changePassword = requestDto.getChangePassword();

        // 현재 비밀번호가 사용자의 비밀번호와 맞는지 검증
        if (!passwordEncoder.matches(password, currentUser.getPassword())) {
            throw new IllegalArgumentException("현재 비밀번호와 사용자의 비밀번호가 일치하지 않습니다.");
        }

        // 변경할 비밀번호와 현재 비밀번호가 동일한지 검증
        if (passwordEncoder.matches(changePassword, currentUser.getPassword())) {
            throw new IllegalArgumentException("동일한 비밀번호로는 변경할 수 없습니다.");
        }
        // 변경할 비밀번호로 수정
        currentUser.setPassword(passwordEncoder.encode(changePassword));
        currentUser.update(requestDto);
        userRepository.save(currentUser);
    }

    private User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 사용자는 존재하지 않습니다.")
        );
    }

    // 사용자 비밀번호 확인 (본인확인)
//    @Transactional
//    public void checkPassword(Long id, ProfileRequestDto requestDto) {
//        User user = findById(id);
//        String inputPassword = passwordEncoder.encode(requestDto.getPassword());
//        if (!passwordEncoder.matches(inputPassword, user.getPassword())) {
//            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
//        }
//    }
}
