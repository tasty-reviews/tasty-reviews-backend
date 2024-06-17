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

/**
 * Spring Security의 UserDetailsService를 구현한 클래스.
 * 사용자 인증(Authentication) 시, 주어진 이메일을 기반으로 데이터베이스에서 회원 정보를 조회하여 UserDetails로 반환한다.
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    /**
     * 주어진 이메일을 기반으로 데이터베이스에서 회원 정보를 조회하여 UserDetails 객체로 반환한다.
     *
     * @param email 조회할 회원의 이메일
     * @return UserDetails 객체
     * @throws UsernameNotFoundException 조회된 회원 정보가 없을 경우 발생하는 예외
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 데이터베이스에서 이메일로 회원 정보 조회
        Optional<Member> memberOptional = memberRepository.findByEmail(email);

        // Optional에서 회원 정보가 존재하는지 확인
        if (memberOptional.isPresent()) {
            // 회원 정보가 존재하면 CustomUserDetails 객체에 담아서 반환
            return new CustomUserDetails(memberOptional.get());
        }

        // 회원 정보가 없으면 UsernameNotFoundException 예외를 던짐
        throw new UsernameNotFoundException("이메일이 존재하지 않습니다. " + email);
    }
}
