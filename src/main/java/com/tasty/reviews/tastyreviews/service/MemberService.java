package com.tasty.reviews.tastyreviews.service;

import com.tasty.reviews.tastyreviews.dto.MemberDTO;
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
    public void join(MemberDTO memberDTO) {

        log.info("암호화 전 : {}", memberDTO.getPassword());

        String encodedPassword = encoder.encode(memberDTO.getPassword());
        memberDTO.setPassword(encodedPassword); // 비밀번호 암호화

        log.info("암호화 후 : {}", encodedPassword);

        memberRepository.save(memberDTO.toEntity()); // 저장
    }
}