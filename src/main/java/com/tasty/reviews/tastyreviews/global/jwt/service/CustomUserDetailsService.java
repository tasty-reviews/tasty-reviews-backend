package com.tasty.reviews.tastyreviews.global.jwt.service;

import com.tasty.reviews.tastyreviews.global.jwt.CustomUserDetails;
import com.tasty.reviews.tastyreviews.member.domain.Member;
import com.tasty.reviews.tastyreviews.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        //DB에서 조회
        Optional<Member> memberOptional = memberRepository.findByEmail(email);

        // Optional에서 회원 정보가 존재하는지 확인
        if (memberOptional.isPresent()) {
            // 회원 정보가 존재하면 UserDetails에 담아서 return하면 AuthenticationManager가 검증 함
            return new CustomUserDetails(memberOptional.get());
        }

        // 회원 정보가 없으면 예외 처리
        throw new UsernameNotFoundException("이메일이 존재하지 않습니다. " + email);
    }
}
