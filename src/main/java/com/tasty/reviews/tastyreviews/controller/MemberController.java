package com.tasty.reviews.tastyreviews.controller;

import com.tasty.reviews.tastyreviews.dto.CreateMemberDTO;
import com.tasty.reviews.tastyreviews.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;

    // 회원가입
    @PostMapping("/join")
    public ResponseEntity<String> join(@RequestBody @Valid CreateMemberDTO createMemberDTO) {
        // 회원 가입 처리
        memberService.join(createMemberDTO);

        return ResponseEntity.ok("회원가입이 완료되었습니다. 이메일을 확인하여 인증해주세요.");
    }
}

