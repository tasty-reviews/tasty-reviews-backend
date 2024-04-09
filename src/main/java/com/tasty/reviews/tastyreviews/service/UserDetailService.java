package com.tasty.reviews.tastyreviews.service;

import com.tasty.reviews.tastyreviews.domain.Member;
import com.tasty.reviews.tastyreviews.dto.CustomUserDetails;
import com.tasty.reviews.tastyreviews.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    //이메일로 회원 조회
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException("해당 사용자가 존재하지 않습니다. : " + email));

        return new CustomUserDetails(member);
    }
}
