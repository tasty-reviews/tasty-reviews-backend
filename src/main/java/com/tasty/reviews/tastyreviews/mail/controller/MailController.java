package com.tasty.reviews.tastyreviews.mail.controller;

import com.tasty.reviews.tastyreviews.mail.dto.MailRequestDTO;
import com.tasty.reviews.tastyreviews.mail.dto.MailResponseDTO;
import com.tasty.reviews.tastyreviews.mail.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MailController {

    private final MailService mailService;

    @PostMapping("/auth-code")
    public ResponseEntity<String> sendAuthCode(@RequestBody MailRequestDTO requestDTO) {
        mailService.sendAuthCode(requestDTO.getAuthCode());

        return ResponseEntity.ok("인증 번호를 전송하였습니다. 인증 번호를 확인해주세요.");
    }

    // 인증 번호 검증
    @PostMapping("/verify")
    public ResponseEntity<String> verifyCode(@RequestBody MailResponseDTO responseDTO) {

        boolean isVerified = mailService.verifyCode(responseDTO.getAuthCode());

        if (isVerified) {
            return ResponseEntity.ok("이메일 인증이 완료되었습니다.");
        } else {
            return ResponseEntity.badRequest().body("인증 번호가 일치하지 않습니다.");
        }
    }
}
