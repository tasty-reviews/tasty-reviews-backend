package com.tasty.reviews.tastyreviews.service;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@RequiredArgsConstructor
@Service
public class MailService {

    private final JavaMailSender emailSender;
    private final HttpSession session;

    @Value("${spring.mail.username}")
    private String fromEmail;

    // 인증 코드 전송 메서드
    public String sendAuthCode(String email) {

        String authCode = generateAuthCode(); // 랜덤 인증 코드 생성
        session.setAttribute("authCode", authCode); // 세션에 인증 코드 저장

        // 이메일 보내기
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(email);
        message.setSubject("회원가입을 위한 인증 코드");
        message.setText("회원가입을 위한 인증 코드는 다음과 같습니다: " + authCode);
        emailSender.send(message);

        return authCode;
    }

    // 인증 코드 검증
    public boolean verifyCode(String inputCode) {
        String authCode = (String) session.getAttribute("authCode");
        return authCode != null && authCode.equals(inputCode);
    }

    // 랜덤 인증 코드 생성
    private String generateAuthCode() {
        int length = 6; // 인증 코드 길이 설정
        String numbers = "0123456789"; // 숫자 범위
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(numbers.charAt(random.nextInt(numbers.length())));
        }
        return sb.toString();
    }
}
