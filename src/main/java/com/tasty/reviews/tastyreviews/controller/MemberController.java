package com.tasty.reviews.tastyreviews.controller;

import com.tasty.reviews.tastyreviews.dto.CreateMemberDTO;
import com.tasty.reviews.tastyreviews.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(produces = "application/json; charset=utf8")
@Slf4j
public class MemberController {

    private final MemberService memberService;

    //회원가입
    @PostMapping("/join")
    public ResponseEntity<String> join(@RequestBody @Valid CreateMemberDTO createMemberDTO) {

        log.info(createMemberDTO.toString());

        memberService.join(createMemberDTO);

        return ResponseEntity.ok("회원가입이 완료되었습니다.");
    }
}