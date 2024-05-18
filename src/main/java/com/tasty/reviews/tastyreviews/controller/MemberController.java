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

        // 회원 가입 처리 (인증 코드와 함께)
        memberService.join(createMemberDTO);

        return ResponseEntity.ok("회원가입이 완료되었습니다. 이메일을 확인하여 인증해주세요.");
    }

    // 인증 코드 확인
    @PostMapping("/verify")
    public ResponseEntity<String> verifyCode(@RequestBody String authCode) {
        // 클라이언트가 입력한 인증 코드와 실제 전송된 코드를 비교하여 인증 여부 확인
        boolean isVerified = mailService.verifyCode(authCode);

        if (isVerified) {
            return ResponseEntity.ok("이메일 인증이 완료되었습니다.");
        } else {
            return ResponseEntity.badRequest().body("인증 코드가 일치하지 않습니다.");
        }
    }
}

