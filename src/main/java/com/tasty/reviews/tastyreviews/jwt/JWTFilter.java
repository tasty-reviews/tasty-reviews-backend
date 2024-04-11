package com.tasty.reviews.tastyreviews.jwt;

import com.tasty.reviews.tastyreviews.domain.Member;
import com.tasty.reviews.tastyreviews.domain.Role;
import com.tasty.reviews.tastyreviews.dto.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //request에서 Authorization 헤더를 찾음
        String authorization = request.getHeader("Authorization");

        //Authorization 헤더 검증
        if (authorization == null || !authorization.startsWith("Bearer ")) {

            System.out.println("token null");
            filterChain.doFilter(request, response);

            //조건이 해당되면 메소드 종료 (필수)
            return;
        }

        System.out.println("authorization now");
        //Bearer 부분 제거 후 순수 토큰만 획득
        String token = authorization.split(" ")[1];

        //토큰 소멸 시간 검증
        if (jwtUtil.isExpired(token)) { //토큰 시간이 만료되었다면 if문 실행

            System.out.println("token expired");
            filterChain.doFilter(request, response);

            //토큰시간이 만료되면 메소드 종료 (필수)
            return;
        }

        //최종 토큰 확인
        String email = jwtUtil.getUsername(token);
        String role = jwtUtil.getRole(token);

        Role roleEnum;
        if ("ROLE_USER".equals(role)) {
            roleEnum = Role.USER;
        } else if ("ROLE_ADMIN".equals(role)) {
            roleEnum = Role.ADMIN;
        } else {
            // 기본 역할 또는 오류 처리
            roleEnum = Role.USER; // 또는 다른 기본 역할
        }

        //member 생성하여 값 set
        Member member = Member.builder()
                .email(email)
                .password("temppassword")
                .role(roleEnum)
                .build();

        //UserDetails에 회원 정보 객체 담기
        CustomUserDetails customUserDetails = new CustomUserDetails(member);

        //스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        //세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);

    }
}
