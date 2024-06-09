package com.sparta.reviewspotproject.service;

import com.sparta.reviewspotproject.dto.EmailRequestDto;
import com.sparta.reviewspotproject.dto.SignupRequestDto;
import com.sparta.reviewspotproject.dto.VerifyCodeRequestDto;
import com.sparta.reviewspotproject.entity.User;
import com.sparta.reviewspotproject.entity.UserStatus;
import com.sparta.reviewspotproject.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender javaMailSender;
    private static final String senderEmail = "ReviewSpot@gmail.com";
    private static int code;
    private final Map<String, String> codes = new HashMap<>();

    // 인증번호 생성
    public static String createCode() {
        int code = (int) (Math.random() * (90000)) + 100000;
        return String.valueOf(code);
    }

    // 이메일 생성
    public MimeMessage createMail(String email, String code) throws MessagingException {
        createCode();
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        try {
            helper.setFrom(senderEmail);
            helper.setTo(email);
            helper.setSubject("ReviewSpot [회원가입을 위한 이메일 인증]");
            String body = "";
            body += "<h3>" + "이메일 인증 번호" + "</h3>";
            body += "<h1>" + code + "</h1>";
            body += "<h3>" + "위 인증번호를 입력해주세요." + "</h3>";
            helper.setText(body, true);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return message;
    }

    // 이메일 전송
    public void sendMail(EmailRequestDto requestDto) throws MessagingException {
        String code = createCode();
        // 인증번호 임시저장
        codes.put(requestDto.getEmail(), code);
        MimeMessage message = createMail(requestDto.getEmail(), code);

        javaMailSender.send(message);
    }

    // 인증번호 검증
    public boolean checkCode(VerifyCodeRequestDto requestDto) {
        String email = requestDto.getEmail();
        String inputCode = requestDto.getVerificationCode();
        String checkCode = codes.get(email);

        if ((!checkCode.isEmpty()) && checkCode.equals(inputCode)) {
            return true;
        } else {
            throw new IllegalArgumentException("인증번호가 일치하지 않거나 만료된 인증번호 입니다.");
        }
    }

    // 회원가입
    public void signup(SignupRequestDto requestDto) {
        String userId = requestDto.getUserId();
        String password = passwordEncoder.encode(requestDto.getPassword());
        String userName = requestDto.getUserName();
        String email = requestDto.getEmail();
        String verificationCode = requestDto.getVerificationCode();
        UserStatus userStatus = UserStatus.NOT_AUTH;

        // 회원 중복 확인
        Optional<User> checkUsername = userRepository.findByUserId(userId);
        if (checkUsername.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        // 이메일 중복 확인
        Optional<User> checkEmail = userRepository.findByEmail(email);
        if (checkEmail.isPresent()) {
            throw new IllegalArgumentException("중복된 Email 입니다.");
        }

        // 이메일 인증 완료 시 회원가입
        VerifyCodeRequestDto verifyCodeRequestDto = new VerifyCodeRequestDto(email, verificationCode);
        if (checkCode(verifyCodeRequestDto)) {
            userStatus = UserStatus.MEMBER;
            User user = new User(userId, password, userName, email, userStatus);
            userRepository.save(user);
        }
    }

    // 회원 탈퇴
    public void setUserStatus(User user) {
        user.setUserStatus(UserStatus.NON_MEMBER);
        userRepository.save(user);
    }

    // 로그아웃
    @Transactional
    public void setNullRefreshToken(User user) {
        user.setRefreshToken(null);
        userRepository.save(user);
    }


}