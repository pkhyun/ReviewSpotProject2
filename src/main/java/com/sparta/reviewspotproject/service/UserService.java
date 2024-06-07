package com.sparta.reviewspotproject.service;

import com.sparta.reviewspotproject.dto.SignupRequestDto;
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
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender javaMailSender;
    private static final String senderEmail = "hhanni0705@gmail.com";
    private static int number;
    private final Map<String, String> codes = new HashMap<>();

    // 인증번호 생성
    public static String createNumber() {
        number = (int)(Math.random()*(90000)) + 100000;
        return String.valueOf(number);
    }

    // 이메일 생성
    public MimeMessage createMail(String mail, String number) throws MessagingException {
        createNumber();
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        try {
            helper.setFrom(senderEmail);
            helper.setTo(mail);
            helper.setSubject("ReviewSpot [회원가입을 위한 이메일 인증]");
            String body = "";
            body += "<h3>" + "이메일 인증 번호" + "</h3>";
            body += "<h1>" + number + "</h1>";
            body += "<h3>" + "위 인증번호를 입력해주세요." + "</h3>";
            helper.setText(body, false);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return message;
    }

    // 이메일 전송
    public void sendMail(String mail) throws MessagingException {
        String code = createNumber();
        // 인증번호 임시저장
        codes.put(mail,code);

        MimeMessage message = createMail(mail,code);
        javaMailSender.send(message);
    }

    // 인증번호 검증
    public boolean checkCode(String email, String inputCode) {
        String checkCode = codes.get(email);
        return checkCode != null && checkCode.equals(inputCode);
    }

    // 회원가입
    public void signup(SignupRequestDto requestDto) {
        String userId = requestDto.getUserId();
        String password = passwordEncoder.encode(requestDto.getPassword());
        String userName = requestDto.getUserName();
        UserStatus userStatus = UserStatus.MEMBER;

        // 회원 중복 확인
        Optional<User> checkUsername = userRepository.findByUserId(userId);
        if (checkUsername.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        // email 중복확인
        String email = requestDto.getEmail();
        Optional<User> checkEmail = userRepository.findByEmail(email);
        if (checkEmail.isPresent()) {
            throw new IllegalArgumentException("중복된 Email 입니다.");
        }

        // 사용자 등록
        User user = new User(userId, password, userName, email, userStatus);
        userRepository.save(user);
    }

    // 로그아웃
    @Transactional
    public void setNullRefreshToken(User user) {
        user.setRefreshToken(null);
        userRepository.save(user);
    }


    // 회원 탈퇴
    public void setUserStatus(User user) {
        user.setUserStatus(UserStatus.NON_MEMBER);
        userRepository.save(user);
    }
}