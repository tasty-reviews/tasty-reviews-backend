package com.tasty.reviews.tastyreviews.member.controller;

import com.tasty.reviews.tastyreviews.member.dto.CreateMemberRequestDTO;
import com.tasty.reviews.tastyreviews.member.dto.CreateMemberResponseDTO;
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
    public ResponseEntity<CreateMemberResponseDTO> join(@RequestBody @Valid CreateMemberRequestDTO createMemberRequestDTO) {
        // 회원 가입 처리
        CreateMemberResponseDTO joinResponse = memberService.join(createMemberRequestDTO);

        return ResponseEntity.ok(joinResponse);
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

