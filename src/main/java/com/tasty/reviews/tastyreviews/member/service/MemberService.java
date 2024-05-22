package com.tasty.reviews.tastyreviews.member.service;

import com.tasty.reviews.tastyreviews.global.jwt.repository.RefreshRepository;
import com.tasty.reviews.tastyreviews.mail.service.MailService;
import com.tasty.reviews.tastyreviews.member.domain.Member;
import com.tasty.reviews.tastyreviews.member.dto.CreatMemberRequsetDTO;
import com.tasty.reviews.tastyreviews.member.dto.CreateMemberResponseDTO;
import com.tasty.reviews.tastyreviews.member.dto.NicknameRequestDTO;
import com.tasty.reviews.tastyreviews.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final MailService mailService;
    private final BCryptPasswordEncoder encoder;
    private final RefreshRepository refreshRepository;


    @Transactional
    public CreateMemberResponseDTO join(CreatMemberRequsetDTO creatMemberRequsetDTO) {

        memberRepository.findByEmail(creatMemberRequsetDTO.getEmail())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 가입된 이메일 입니다");
                });

        memberRepository.findByNickname(creatMemberRequsetDTO.getNickname())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 닉네임 입니다.");
                });

        // 중복된 이메일이 존재하지 않을 경우 회원가입 진행
        log.info("암호화 전 : {}", creatMemberRequsetDTO.getPassword());

        String encodedPassword = encoder.encode(creatMemberRequsetDTO.getPassword());
        creatMemberRequsetDTO.setPassword(encodedPassword); // 비밀번호 암호화

        log.info("암호화 후 : {}", encodedPassword);

        Member joinMember = memberRepository.save(creatMemberRequsetDTO.toEntity());// 저장

        return new CreateMemberResponseDTO(joinMember); //응답 DTO 리턴
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
}
