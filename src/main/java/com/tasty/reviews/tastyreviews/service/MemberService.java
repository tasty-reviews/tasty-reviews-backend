package com.tasty.reviews.tastyreviews.service;

import com.tasty.reviews.tastyreviews.dto.CreateMemberDTO;
import com.tasty.reviews.tastyreviews.repository.MemberRepository;
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
    private final BCryptPasswordEncoder encoder;

    @Transactional
    public void join(CreateMemberDTO createMemberDTO) {

        memberRepository.findByEmail(createMemberDTO.getEmail())
                .ifPresent( m -> {
                    throw new IllegalStateException("이미 가입된 이메일 입니다");
                });

        memberRepository.findByNickname(createMemberDTO.getNickname())
                .ifPresent( m -> {
                    throw new IllegalStateException("이미 존재하는 닉네임 입니다.");
                });

        //중복된 이메일이 존재하지 않을경우 회원가입 진행
        log.info("암호화 전 : {}", createMemberDTO.getPassword());

        String encodedPassword = encoder.encode(createMemberDTO.getPassword());
        createMemberDTO.setPassword(encodedPassword); // 비밀번호 암호화

        log.info("암호화 후 : {}", encodedPassword);

        memberRepository.save(createMemberDTO.toEntity()); // 저장
    }

}
