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

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final MailService mailService;
    private final BCryptPasswordEncoder encoder;
    private final RefreshRepository refreshRepository;
    private final ReviewRepository reviewRepository;
//    private final ModelMapper modelMapper;

    @Transactional
    public CreateMemberResponseDTO join(CreateMemberRequestDTO createMemberRequestDTO) {

        memberRepository.findByEmail(createMemberRequestDTO.getEmail())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 가입된 이메일 입니다");
                });

        memberRepository.findByNickname(createMemberRequestDTO.getNickname())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 닉네임 입니다.");
                });

        String encodedPassword = encoder.encode(createMemberRequestDTO.getPassword());
        createMemberRequestDTO.setPassword(encodedPassword); // 비밀번호 암호화

        Member joinMember = memberRepository.save(createMemberRequestDTO.toEntity());// 저장

        return new CreateMemberResponseDTO(joinMember); //응답 DTO 리턴
    }

    @Transactional(readOnly = true)
    public MemberResponseDTO myPage(Long id) {

        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("일치하는 회원이 없습니다."));

        return MemberResponseDTO.builder()
                .id(member.getId())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .reviewList(member.getReviews())
                .build();
    }

    @Transactional
    public void updateNickname(NicknameRequestDTO requestDTO, Long id) {

        //해당하는 멤버 찾기
        Member findMember = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다"));

        //해당하는 멤버의 닉네임 수정
        findMember.updateNickname(requestDTO.getNickName());
    }

    @Transactional
    public void deleteMember(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다"));

        //해당하는 회원의 리프레시 토큰 삭제
        refreshRepository.deleteByUserEmail(member.getEmail());

        //회원 삭제
        memberRepository.delete(member);
    }

    @Transactional
    public void updatePassword(String email, String currentPassword, String newPassword) {
        Member member = validatePassword(email, currentPassword); //비밀번호 검증
        member.updatePassword(encoder.encode(newPassword)); //새로운 비밀번호 암호화
    }

    //비밀번호 검증 로직
    private Member validatePassword(String email, String currentPassword) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException(""));

        if (!encoder.matches(currentPassword, member.getPassword())) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }
        return member;
    }
}


