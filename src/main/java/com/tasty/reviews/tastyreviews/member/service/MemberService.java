package com.tasty.reviews.tastyreviews.member.service;

import com.tasty.reviews.tastyreviews.global.jwt.repository.RefreshRepository;
import com.tasty.reviews.tastyreviews.mail.service.MailService;
import com.tasty.reviews.tastyreviews.member.domain.Member;
import com.tasty.reviews.tastyreviews.member.dto.CreateMemberRequestDTO;
import com.tasty.reviews.tastyreviews.member.dto.CreateMemberResponseDTO;
import com.tasty.reviews.tastyreviews.member.dto.MemberResponseDTO;
import com.tasty.reviews.tastyreviews.member.dto.NicknameRequestDTO;
import com.tasty.reviews.tastyreviews.member.repository.MemberRepository;
import com.tasty.reviews.tastyreviews.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 회원 관리 서비스 클래스입니다.
 */
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final MailService mailService;
    private final BCryptPasswordEncoder encoder;
    private final RefreshRepository refreshRepository;
    private final ReviewRepository reviewRepository;

    /**
     * 회원 가입 메서드입니다.
     *
     * @param createMemberRequestDTO 회원 가입 요청 DTO
     * @return 회원 가입 응답 DTO
     */
    @Transactional
    public CreateMemberResponseDTO join(CreateMemberRequestDTO createMemberRequestDTO) {
        // 이메일 중복 체크
        memberRepository.findByEmail(createMemberRequestDTO.getEmail())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 가입된 이메일입니다.");
                });

        // 닉네임 중복 체크
        memberRepository.findByNickname(createMemberRequestDTO.getNickname())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 닉네임입니다.");
                });

        // 비밀번호 암호화
        String encodedPassword = encoder.encode(createMemberRequestDTO.getPassword());
        createMemberRequestDTO.setPassword(encodedPassword);

        // 회원 저장
        Member joinedMember = memberRepository.save(createMemberRequestDTO.toEntity());

        // 회원 가입 응답 DTO 반환
        return new CreateMemberResponseDTO(joinedMember);
    }

    /**
     * 내 정보 조회 메서드입니다.
     *
     * @param id 조회할 회원의 ID
     * @return 회원 정보 응답 DTO
     */
    @Transactional(readOnly = true)
    public MemberResponseDTO myPage(Long id) {
        // 회원 조회
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID에 해당하는 회원이 없습니다."));

        // 회원 정보 응답 DTO 생성
        return MemberResponseDTO.builder()
                .id(member.getId())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .reviewList(member.getReviews())
                .build();
    }

    /**
     * 닉네임 수정 메서드입니다.
     *
     * @param requestDTO 닉네임 수정 요청 DTO
     * @param id         회원 ID
     */
    @Transactional
    public void updateNickname(NicknameRequestDTO requestDTO, Long id) {
        // 회원 조회
        Member findMember = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID에 해당하는 회원이 없습니다."));

        // 닉네임 업데이트
        findMember.updateNickname(requestDTO.getNickName());
    }

    /**
     * 회원 탈퇴 메서드입니다.
     *
     * @param id 탈퇴할 회원의 ID
     */
    @Transactional
    public void deleteMember(Long id) {
        // 회원 조회
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID에 해당하는 회원이 없습니다."));

        // 회원의 리프레시 토큰 삭제
        refreshRepository.deleteByUserEmail(member.getEmail());

        // 회원 삭제
        memberRepository.delete(member);
    }

    /**
     * 비밀번호 업데이트 메서드입니다.
     *
     * @param email           이메일
     * @param currentPassword 현재 비밀번호
     * @param newPassword     새로운 비밀번호
     */
    @Transactional
    public void updatePassword(String email, String currentPassword, String newPassword) {
        // 비밀번호 검증
        Member member = validatePassword(email, currentPassword);

        // 새로운 비밀번호 암호화
        member.updatePassword(encoder.encode(newPassword));
    }

    /**
     * 비밀번호 유효성 검사 및 회원 반환 메서드입니다.
     *
     * @param email           이메일
     * @param currentPassword 현재 비밀번호
     * @return 검증된 회원 객체
     * @throws IllegalArgumentException 현재 비밀번호가 일치하지 않는 경우 예외 발생
     */
    private Member validatePassword(String email, String currentPassword) {
        // 이메일로 회원 조회
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일에 해당하는 회원이 없습니다."));

        // 현재 비밀번호 검증
        if (!encoder.matches(currentPassword, member.getPassword())) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }

        return member;
    }
}
