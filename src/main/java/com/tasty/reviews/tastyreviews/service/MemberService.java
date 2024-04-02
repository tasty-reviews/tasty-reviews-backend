package com.tasty.reviews.tastyreviews.service;

import com.tasty.reviews.tastyreviews.dto.CreateMemberDTO;
import com.tasty.reviews.tastyreviews.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder encoder;

    @Transactional
    public void join(CreateMemberDTO createMemberDTO) {
        //비밀번호 암호화
        createMemberDTO.setPassword(encoder.encode(createMemberDTO.getPassword()));
        memberRepository.save(createMemberDTO.toEntity());
    }
}
