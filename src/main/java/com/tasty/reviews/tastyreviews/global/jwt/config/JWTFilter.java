package com.tasty.reviews.tastyreviews.global.jwt.config;

// 패키지 선언부: 현재 클래스가 속한 패키지를 정의합니다.
// 패키지 이름은 com.tasty.reviews.tastyreviews.global.jwt.config입니다.

import com.tasty.reviews.tastyreviews.global.jwt.CustomUserDetails;
import com.tasty.reviews.tastyreviews.global.jwt.service.JWTUtil;
import com.tasty.reviews.tastyreviews.member.domain.Member;
import com.tasty.reviews.tastyreviews.member.domain.Role;
import io.jsonwebtoken.ExpiredJwtException;
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
import java.io.PrintWriter;

// 필요한 클래스들을 임포트합니다.
// JWT 유틸리티 클래스, 사용자 도메인 클래스들, JWT 예외 클래스, 서블릿 관련 클래스들입니다.

@RequiredArgsConstructor
// @RequiredArgsConstructor 어노테이션은 final 필드에 대한 생성자를 자동으로 생성합니다.
public class JWTFilter extends OncePerRequestFilter {
    // JWTFilter 클래스는 OncePerRequestFilter를 상속받아 필터 기능을 구현합니다.

    private final JWTUtil jwtUtil;
    // JWT 유틸리티 클래스 인스턴스입니다.

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // doFilterInternal 메서드는 요청을 필터링하는 메서드입니다.

        // 헤더에서 access키에 담긴 토큰을 꺼냅니다.
        String accessToken = request.getHeader("access");

        // 토큰이 없다면 다음 필터로 넘깁니다.
        if (accessToken == null) {
            System.out.println("null");
            filterChain.doFilter(request, response);
            return;
        }

        // 토큰 만료 여부를 확인합니다. 만료 시 다음 필터로 넘기지 않습니다.
        try {
            jwtUtil.isExpired(accessToken);
        } catch (ExpiredJwtException e) {
            // 응답 본문에 토큰 만료 메시지를 작성합니다.
            PrintWriter writer = response.getWriter();
            writer.print("토큰이 만료 되었습니다");

            // 응답 상태 코드를 401 (Unauthorized)로 설정합니다.
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // 토큰이 "access"인지 확인합니다 (발급 시 페이로드에 명시).
        String category = jwtUtil.getCategory(accessToken);

        if (!category.equals("access")) {
            // 응답 본문에 잘못된 토큰 메시지를 작성합니다.
            PrintWriter writer = response.getWriter();
            writer.print("invalid access token");

            // 응답 상태 코드를 401 (Unauthorized)로 설정합니다.
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 프론트와 상태코드를 협의해야 합니다.
            return;
        }

        // 토큰에서 username과 role 값을 획득합니다.
        String email = jwtUtil.getUsername(accessToken);
        String role = jwtUtil.getRole(accessToken);

        // role 값을 기반으로 Role enum을 설정합니다.
        Role roleEnum;
        if ("ROLE_USER".equals(role)) {
            roleEnum = Role.USER;
        } else if ("ROLE_ADMIN".equals(role)) {
            roleEnum = Role.ADMIN;
        } else {
            // 기본 역할 또는 오류 처리
            roleEnum = Role.USER; // 또는 다른 기본 역할
        }

        // Member 객체를 생성합니다.
        Member member = Member.builder()
                .email(email)
                .password("temppassword") // 비밀번호는 임시 값으로 설정합니다.
                .role(roleEnum)
                .build();

        // CustomUserDetails 객체를 생성합니다.
        CustomUserDetails customUserDetails = new CustomUserDetails(member);

        // Authentication 객체를 생성하고 SecurityContext에 설정합니다.
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        // 다음 필터로 요청을 전달합니다.
        filterChain.doFilter(request, response);
    }
}
