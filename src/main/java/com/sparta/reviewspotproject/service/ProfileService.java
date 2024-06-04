package com.sparta.reviewspotproject.service;

import com.sparta.reviewspotproject.dto.ProfileRequestDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileService {

//    @Transactional
//    public void updateProfile(Long id, ProfileRequestDto requestDto, User user) {
//        User user = findUser(id);
//        String password = requestDto.getPassword();
//        String changePassword = requestDto.getChangePassword();
//
//        // 본인확인을 위한 비밀번호 일치 & 변경하려는 비밀번호와 현재 비밀번호 확인
//        if (checkPWAndFindUser(requestDto,user) && !changePassword.equals(user.getPassword())) {
//            user.update(requestDto);
//        }
//    }
//
//    private boolean checkPWAndFindUser(ProfileRequestDto requestDto, User user) {
//        if (user.getPassword().equals(requestDto.getPassword())) {
//            return true;
//        } else {
//            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
//        }
//    }
//
//    private User findUser(Long id) {
//        return userRepository.findById(id).orElseThrow(() ->
//                new IllegalArgumentException("해당 사용자는 존재하지 않습니다.")
//        );
//    }
}