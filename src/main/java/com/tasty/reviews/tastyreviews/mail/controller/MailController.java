package com.tasty.reviews.tastyreviews.mail.controller;

import com.tasty.reviews.tastyreviews.mail.dto.MailRequestDTO;
import com.tasty.reviews.tastyreviews.mail.dto.MailResponseDTO;
import com.tasty.reviews.tastyreviews.mail.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 이메일 전송과 인증 코드 검증을 처리하는 컨트롤러입니다.
 */
@RequiredArgsConstructor
@RestController
public class MailController {

    private final MailService mailService;

    /**
     * 인증 코드를 이메일로 전송하는 엔드포인트입니다.
     *
     * @param requestDTO 전송할 인증 코드가 담긴 DTO 객체
     * @return 인증 코드 전송 성공 메시지
     */
    @PostMapping("/auth-code")
    public ResponseEntity<String> sendAuthCode(@RequestBody MailRequestDTO requestDTO) {
        mailService.sendAuthCode(requestDTO.getAuthCode());

        return ResponseEntity.ok("인증 번호를 전송하였습니다. 인증 번호를 확인해주세요.");
    }

    /**
     * 사용자가 입력한 인증 코드를 검증하는 엔드포인트입니다.
     *
     * @param responseDTO 사용자가 입력한 인증 코드가 담긴 DTO 객체
     * @return 인증 코드 검증 결과 메시지
     */
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
