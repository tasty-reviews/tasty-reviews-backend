package com.tasty.reviews.tastyreviews.global.jwt.config;

// 패키지 선언부: 현재 클래스가 속한 패키지를 정의합니다.
// 패키지 이름은 com.tasty.reviews.tastyreviews.global.jwt.config입니다.

import com.tasty.reviews.tastyreviews.global.jwt.service.JWTUtil;
import com.tasty.reviews.tastyreviews.global.jwt.repository.RefreshRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

// 필요한 클래스들을 임포트합니다.
// JWT 유틸리티 클래스와 리프레시 토큰 저장소, JWT 예외 클래스, 서블릿 관련 클래스들입니다.

@RequiredArgsConstructor
// @RequiredArgsConstructor 어노테이션은 final 필드에 대한 생성자를 자동으로 생성합니다.
public class CustomLogoutFilter extends GenericFilterBean {
    // CustomLogoutFilter 클래스는 GenericFilterBean을 상속받아 필터 기능을 구현합니다.

    private final JWTUtil jwtUtil;
    // JWT 유틸리티 클래스 인스턴스입니다.
    private final RefreshRepository refreshRepository;
    // 리프레시 토큰 저장소 인스턴스입니다.

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // doFilter 메서드는 필터 체인에서 요청을 처리하는 메서드입니다.
        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
        // 요청과 응답 객체를 HttpServletRequest와 HttpServletResponse로 캐스팅하여 doFilter 메서드를 호출합니다.
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        // 실제 필터 로직을 처리하는 메서드입니다.

        // 로그아웃 엔드포인트와 메서드가 맞는지 검증합니다.
        String requestUri = request.getRequestURI();
        if (!requestUri.matches("^\\/logout$")) {
            // 요청 URI가 "/logout"이 아닌 경우, 다음 필터로 요청을 전달합니다.
            filterChain.doFilter(request, response);
            return;
        }
        String requestMethod = request.getMethod();
        if (!requestMethod.equals("POST")) {
            // 요청 메서드가 POST가 아닌 경우, 다음 필터로 요청을 전달합니다.
            filterChain.doFilter(request, response);
            return;
        }

        // 쿠키에서 리프레시 토큰을 가져옵니다.
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh")) {
                refresh = cookie.getValue();
            }
        }

        // 리프레시 토큰이 없는 경우
        if (refresh == null) {
            // 응답 상태 코드를 400 (Bad Request)로 설정합니다.
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // 리프레시 토큰이 만료된 경우
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            // 응답 상태 코드를 400 (Bad Request)로 설정합니다.
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // 토큰이 "refresh" 카테고리인지 확인합니다.
        String category = jwtUtil.getCategory(refresh);
        if (!category.equals("refresh")) {
            // 응답 상태 코드를 400 (Bad Request)로 설정합니다.
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // 리프레시 토큰이 DB에 저장되어 있는지 확인합니다.
        Boolean isExist = refreshRepository.existsByRefresh(refresh);
        if (!isExist) {
            // 응답 상태 코드를 400 (Bad Request)로 설정합니다.
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // 로그아웃 진행
        // 리프레시 토큰을 DB에서 제거합니다.
        refreshRepository.deleteByRefresh(refresh);

        // 리프레시 토큰 쿠키를 제거합니다.
        Cookie cookie = new Cookie("refresh", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");

        response.addCookie(cookie);
        // 응답 상태 코드를 200 (OK)로 설정합니다.
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
