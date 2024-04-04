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

        log.info("암호화 전 : {}", createMemberDTO.getPassword());

        String encodedPassword = encoder.encode(createMemberDTO.getPassword());
        createMemberDTO.setPassword(encodedPassword); // 비밀번호 암호화

        log.info("암호화 후 : {}", encodedPassword);

        memberRepository.save(createMemberDTO.toEntity()); // 저장
    }
}
