package com.tasty.reviews.tastyreviews.service;

import com.tasty.reviews.tastyreviews.domain.Gender;
import com.tasty.reviews.tastyreviews.domain.Member;
import com.tasty.reviews.tastyreviews.dto.CreateMemberDTO;
import com.tasty.reviews.tastyreviews.repository.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class CreateMemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Test
    @DisplayName("회원가입 테스트")

    void 회원가입_테스트() {
        //given(준비)
//        LocalDateTime now = LocalDateTime.now();

        CreateMemberDTO createMemberDTO = new CreateMemberDTO();

        createMemberDTO.setEmail("이메일1");
        createMemberDTO.setPassword("비밀번호");
        createMemberDTO.setNickname("닉네임1");
        createMemberDTO.setAge(10);
        createMemberDTO.setGender(Gender.MALE);

        //when(실행)
        memberRepository.save(createMemberDTO.toEntity());

        //then(검증)
        Member saveMember = memberRepository.findByEmail("이메일1");
        Assertions.assertNotNull(saveMember);

        assertEquals("이메일1", saveMember.getEmail());
        assertEquals("비밀번호", saveMember.getPassword());
        assertEquals("닉네임1", saveMember.getNickname());
        assertEquals(10, saveMember.getAge());
//        assertEquals("MALE", saveMember.getGender());
    }

}
