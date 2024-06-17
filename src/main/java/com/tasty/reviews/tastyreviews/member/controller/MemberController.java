package com.tasty.reviews.tastyreviews.member.controller;

import com.tasty.reviews.tastyreviews.member.dto.*;
import com.tasty.reviews.tastyreviews.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * 회원 관련 API 엔드포인트를 정의하는 컨트롤러 클래스입니다.
 */
@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService; // 회원 서비스 클래스 의존성 주입

    /**
     * 회원 가입 처리를 위한 엔드포인트입니다.
     * @param createMemberRequestDTO 회원 가입 요청 DTO 객체
     * @return 회원 가입 응답 DTO 객체
     */
    @PostMapping("/join")
    public ResponseEntity<CreateMemberResponseDTO> join(@RequestBody @Valid CreateMemberRequestDTO createMemberRequestDTO) {
        CreateMemberResponseDTO joinResponse = memberService.join(createMemberRequestDTO); // 회원 가입 처리
        return ResponseEntity.ok(joinResponse); // 처리 결과 반환
    }

    /**
     * 특정 회원 정보 조회를 위한 엔드포인트입니다.
     * @param id 조회할 회원의 ID
     * @return 조회된 회원 정보 DTO 객체
     */
    @GetMapping("/mypage/{id}")
    public ResponseEntity<MemberResponseDTO> myPage(@PathVariable Long id) {
        MemberResponseDTO member = memberService.myPage(id); // 회원 정보 조회
        return ResponseEntity.ok(member); // 조회된 회원 정보 반환
    }

    /**
     * 회원의 닉네임 변경을 위한 엔드포인트입니다.
     * @param requestDTO 변경할 닉네임 요청 DTO 객체
     * @param id 닉네임을 변경할 회원의 ID
     * @return 변경된 닉네임 요청 DTO 객체
     */
    @PatchMapping("/mypage/{id}/setting/nickname")
    public ResponseEntity<NicknameRequestDTO> updateNickname(@RequestBody NicknameRequestDTO requestDTO,
                                                             @PathVariable Long id) {
        memberService.updateNickname(requestDTO, id); // 닉네임 변경 처리
        return ResponseEntity.ok(requestDTO); // 변경된 닉네임 요청 DTO 반환
    }

    /**
     * 회원 탈퇴를 위한 엔드포인트입니다.
     * @param id 탈퇴할 회원의 ID
     * @return 탈퇴 완료 메시지
     */
    @DeleteMapping("/mypage/{id}/setting/delete")
    public ResponseEntity<String> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id); // 회원 탈퇴 처리
        return ResponseEntity.ok("회원탈퇴가 완료되었습니다."); // 탈퇴 완료 메시지 반환
    }

    /**
     * 회원의 비밀번호를 변경하기 위한 엔드포인트입니다.
     * @param requestPasswordDTO 비밀번호 변경 요청 DTO 객체
     * @return 비밀번호 변경 완료 메시지
     */
    @PatchMapping("/mypage/setting/password")
    public ResponseEntity<String> updatePassword(@RequestBody @Valid UpdateRequestPasswordDTO requestPasswordDTO) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName(); // 현재 인증된 사용자의 이메일 가져오기
        memberService.updatePassword(email, requestPasswordDTO.getCurrentPassword(), requestPasswordDTO.getNewPassword()); // 비밀번호 변경 처리
        return ResponseEntity.ok("비밀번호 변경이 완료되었습니다."); // 변경 완료 메시지 반환
    }
}
