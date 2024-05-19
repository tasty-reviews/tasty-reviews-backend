package com.tasty.reviews.tastyreviews.controller;

import com.tasty.reviews.tastyreviews.dto.CreateMemberDTO;
import com.tasty.reviews.tastyreviews.service.MailService;
import com.tasty.reviews.tastyreviews.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequiredArgsConstructor
@Slf4j
@RestController
public class MemberController {

    private final MemberService memberService;
    private final MailService mailService;

    // 회원가입
    @PostMapping("/join")
    public ResponseEntity<String> join(@RequestBody @Valid CreateMemberDTO createMemberDTO) {
        log.info(createMemberDTO.toString());

        // 회원 가입 처리
        memberService.join(createMemberDTO);

        return ResponseEntity.ok("회원가입이 완료되었습니다. 이메일을 확인하여 인증해주세요.");
    }

    // 인증 코드 발송
    @PostMapping("/auth-code")
    public ResponseEntity<String> sendAuthCode(@RequestBody Map<String, String> requestBody) {
        String authCode = requestBody.get("inputMail");
        mailService.sendAuthCode(authCode);

        return ResponseEntity.ok("인증 번호를 전송하였습니다. 인증 번호를 확인해주세요.");
    }

    // 인증 코드 확인
    @PostMapping("/verify")
    public ResponseEntity<String> verifyCode(@RequestBody Map<String, String> requestBody) {
        String authCode = requestBody.get("inputCode");
        boolean isVerified = mailService.verifyCode(authCode);

        if (isVerified) {
            return ResponseEntity.ok("이메일 인증이 완료되었습니다.");
        } else {
            return ResponseEntity.badRequest().body("인증 번호가 일치하지 않습니다.");
        }
    }

}

