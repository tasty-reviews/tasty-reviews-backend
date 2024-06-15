package com.tasty.reviews.tastyreviews.global.jwt.controller;

import com.tasty.reviews.tastyreviews.global.jwt.service.JWTUtil;
import com.tasty.reviews.tastyreviews.global.jwt.damain.RefreshEntity;
import com.tasty.reviews.tastyreviews.global.jwt.repository.RefreshRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController // REST API 컨트롤러임을 나타냄
@RequiredArgsConstructor // final 필드에 대한 생성자를 자동으로 생성
public class ReissueController { // 토큰 재발급 컨트롤러

    private final JWTUtil jwtUtil; // JWT 유틸리티 클래스
    private final RefreshRepository refreshRepository; // 리프레시 토큰 저장소

    @PostMapping("/reissue") // 토큰 재발급 엔드포인트
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {

        // 리프레시 토큰 가져오기
        String refresh = null;
        Cookie[] cookies = request.getCookies(); // 요청에서 쿠키를 가져옴
        for (Cookie cookie : cookies) {

            if (cookie.getName().equals("refresh")) { // 쿠키 이름이 "refresh"인 쿠키를 찾음
                refresh = cookie.getValue(); // 리프레시 토큰 값 설정
            }
        }

        if (refresh == null) {
            // 리프레시 토큰이 없으면 400 BAD_REQUEST 응답
            return new ResponseEntity<>("refresh token null", HttpStatus.BAD_REQUEST);
        }

        // 리프레시 토큰 만료 확인
        try {
            jwtUtil.isExpired(refresh); // 토큰 만료 여부 체크
        } catch (ExpiredJwtException e) {
            // 토큰이 만료되었으면 400 BAD_REQUEST 응답
            return new ResponseEntity<>("refresh token expired", HttpStatus.BAD_REQUEST);
        }

        // 토큰이 리프레시 토큰인지 확인 (발급 시 페이로드에 명시)
        String category = jwtUtil.getCategory(refresh);

        if (!category.equals("refresh")) {
            // 리프레시 토큰이 아니면 400 BAD_REQUEST 응답
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        // DB에 저장되어 있는지 확인
        Boolean isExist = refreshRepository.existsByRefresh(refresh);
        if (!isExist) {
            // 리프레시 토큰이 DB에 없으면 400 BAD_REQUEST 응답
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        String email = jwtUtil.getUsername(refresh); // 리프레시 토큰에서 이메일 가져옴
        String role = jwtUtil.getRole(refresh); // 리프레시 토큰에서 역할 가져옴

        // 새로운 액세스 토큰 생성
        String newAccess = jwtUtil.createJwt("access", email, role, 600000L);
        // 새로운 리프레시 토큰 생성
        String newRefresh = jwtUtil.createJwt("refresh", email, role, 86400000L);

        // DB에 기존의 리프레시 토큰 삭제 후 새 리프레시 토큰 저장
        refreshRepository.deleteByRefresh(refresh);
        addRefreshEntity(email, newRefresh, 86400000L);

        // 응답에 새로운 토큰 설정
        response.setHeader("access", newAccess);
        response.addCookie(createCookie("refresh", newRefresh));

        return new ResponseEntity<>(HttpStatus.OK); // 200 OK 응답
    }

    // 쿠키 생성 메서드
    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24 * 60 * 60); // 쿠키 유효기간 설정 (24시간)
        // cookie.setSecure(true); // HTTPS를 통해서만 전송되도록 설정 (주석 처리됨)
        // cookie.setPath("/"); // 쿠키의 경로 설정 (주석 처리됨)
        cookie.setHttpOnly(true); // HttpOnly 설정 (자바스크립트에서 접근 불가)

        return cookie;
    }

    // 새 리프레시 엔티티 추가 메서드
    private void addRefreshEntity(String userEmail, String refresh, Long expiredMs) {
        Date date = new Date(System.currentTimeMillis() + expiredMs); // 만료 일자 설정

        RefreshEntity refreshEntity = new RefreshEntity(); // 리프레시 엔티티 생성
        refreshEntity.setUserEmail(userEmail); // 이메일 설정
        refreshEntity.setRefresh(refresh); // 리프레시 토큰 설정
        refreshEntity.setExpiration(date.toString()); // 만료 일자 설정

        refreshRepository.save(refreshEntity); // DB에 저장
    }
}
