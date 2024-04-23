package com.tasty.reviews.tastyreviews.jwt;

import com.tasty.reviews.tastyreviews.repository.RefreshRepository;
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

@RequiredArgsConstructor
public class CustomLogoutFilter extends GenericFilterBean {

    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        // 로그아웃 엔드포인트와 메서드가 맞는지 검증
        String requestUri = request.getRequestURI();
        if (!requestUri.matches("^\\/logout$")) {
            // 다음 필터로 요청 전달
            filterChain.doFilter(request, response);
            return;
        }
        String requestMethod = request.getMethod();
        if (!requestMethod.equals("POST")) {
            // 다음 필터로 요청 전달
            filterChain.doFilter(request, response);
            return;
        }

        // 쿠키에서 리프레시 토큰 가져오기
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh")) {
                refresh = cookie.getValue();
            }
        }

        // 리프레시 토큰이 없는 경우
        if (refresh == null) {
            // 응답 상태 코드 설정
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // 리프레시 토큰이 만료된 경우
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            // 응답 상태 코드 설정
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // 토큰이 refresh인지 확인
        String category = jwtUtil.getCategory(refresh);
        if (!category.equals("refresh")) {
            // 응답 상태 코드 설정
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // DB에 저장되어 있는지 확인
        Boolean isExist = refreshRepository.existsByRefresh(refresh);
        if (!isExist) {
            // 응답 상태 코드 설정
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // 로그아웃 진행
        // Refresh 토큰 DB에서 제거
        refreshRepository.deleteByRefresh(refresh);

        // Refresh 토큰 쿠키 값 0으로 설정하여 제거
        Cookie cookie = new Cookie("refresh", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");

        response.addCookie(cookie);
        // 응답 상태 코드 설정
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
