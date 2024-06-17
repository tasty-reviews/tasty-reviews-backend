package com.tasty.reviews.tastyreviews.mail.service;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * 이메일 발송 및 인증 관련 서비스 클래스입니다.
 */
@RequiredArgsConstructor
@Service
public class MailService {

    private final JavaMailSender emailSender; // 이메일 발송 객체
    private final HttpSession session; // 세션 객체

    @Value("${spring.mail.username}")
    private String fromEmail; // 발신자 이메일 주소

    /**
     * 인증 코드를 생성하고 이메일로 전송하는 메서드입니다.
     * @param email 수신할 이메일 주소
     */
    public void sendAuthCode(String email) {
        String authCode = generateAuthCode(); // 랜덤 인증 코드 생성
        session.setAttribute("authCode", authCode); // 세션에 인증 코드 저장

        // 이메일 보내기
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail); // 발신자 설정
        message.setTo(email); // 수신자 설정
        message.setSubject("회원가입을 위한 인증 코드"); // 이메일 제목
        message.setText("회원가입을 위한 인증 코드는 다음과 같습니다: " + authCode); // 이메일 내용
        emailSender.send(message); // 이메일 전송
    }

    /**
     * 입력된 인증 코드가 세션에 저장된 코드와 일치하는지 확인하는 메서드입니다.
     * @param inputCode 사용자가 입력한 인증 코드
     * @return 인증 코드 일치 여부 (true: 일치, false: 불일치)
     */
    public boolean verifyCode(String inputCode) {
        String authCode = (String) session.getAttribute("authCode"); // 세션에서 저장된 인증 코드 가져오기
        return authCode != null && authCode.equals(inputCode); // 입력된 코드와 세션의 코드 비교하여 일치 여부 반환
    }

    /**
     * 6자리의 랜덤 인증 코드를 생성하는 메서드입니다.
     * @return 생성된 랜덤 인증 코드
     */
    private String generateAuthCode() {
        int length = 6; // 인증 코드 길이 설정
        String numbers = "0123456789"; // 숫자 범위
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(numbers.charAt(random.nextInt(numbers.length()))); // 숫자 범위 내에서 랜덤으로 문자 선택하여 추가
        }
        return sb.toString(); // 생성된 인증 코드 반환
    }
}
