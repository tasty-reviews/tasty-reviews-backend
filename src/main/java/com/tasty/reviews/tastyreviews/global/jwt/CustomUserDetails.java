package com.tasty.reviews.tastyreviews.global.jwt;

import com.tasty.reviews.tastyreviews.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Spring Security에서 사용할 사용자 정보를 제공하는 클래스입니다.
 * CustomUserDetails는 Member 엔티티를 기반으로 UserDetails를 구현하여 사용자 정보를 제공합니다.
 */
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final Member member; // 사용자 정보를 담고 있는 Member 엔티티

    /**
     * 사용자의 비밀번호를 반환합니다.
     *
     * @return 사용자의 비밀번호
     */
    @Override
    public String getPassword() {
        return member.getPassword();
    }

    /**
     * 사용자의 이메일 주소를 반환합니다.
     *
     * @return 사용자의 이메일 주소
     */
    @Override
    public String getUsername() {
        return member.getEmail();
    }

    /**
     * 계정이 만료되지 않았음을 반환합니다 (항상 true).
     *
     * @return 계정의 만료 여부
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 계정이 잠기지 않았음을 반환합니다 (항상 true).
     *
     * @return 계정의 잠금 여부
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 자격 증명이 만료되지 않았음을 반환합니다 (항상 true).
     *
     * @return 자격 증명의 만료 여부
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 계정이 활성화되어 있음을 반환합니다 (항상 true).
     *
     * @return 계정의 활성화 여부
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * 사용자의 권한 정보를 반환합니다.
     * Member 엔티티에 저장된 역할 정보를 기반으로 "ROLE_" 접두사를 붙여 반환합니다.
     *
     * @return 사용자의 권한 정보
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(() -> "ROLE_" + member.getRole()); // 사용자의 역할 정보를 GrantedAuthority로 추가
        return authorities;
    }
}
