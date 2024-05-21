package com.tasty.reviews.tastyreviews.member.controller;

import com.tasty.reviews.tastyreviews.member.dto.CreateMemberDTO;
import com.tasty.reviews.tastyreviews.member.dto.NicknameRequestDTO;
import com.tasty.reviews.tastyreviews.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;

    // 회원가입
    @PostMapping("/join")
    public ResponseEntity<CreateMemberDTO> join(@RequestBody @Valid CreateMemberDTO createMemberDTO) {
        // 회원 가입 처리
        memberService.join(createMemberDTO);

        return ResponseEntity.ok(createMemberDTO);
    }

    //닉네임 변경
    @PatchMapping("/mypage/setting/{id}/nickname")
    public ResponseEntity<NicknameRequestDTO> updateNickname(@RequestBody NicknameRequestDTO requestDTO,
                                                             @PathVariable Long id) {
        memberService.updateNickname(requestDTO, id);

        return ResponseEntity.ok(requestDTO);
    }

    //회원 탈퇴
    @DeleteMapping("/mypage/setting/{id}/delete")
    public ResponseEntity<String> deleteMember(@PathVariable Long id) {

        memberService.deleteMember(id);

        return ResponseEntity.ok("회원탈퇴가 완료되었습니다");
    }
}

