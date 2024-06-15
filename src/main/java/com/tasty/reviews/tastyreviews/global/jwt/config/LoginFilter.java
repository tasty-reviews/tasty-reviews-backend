package com.tasty.reviews.tastyreviews.global.jwt.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tasty.reviews.tastyreviews.global.jwt.service.JWTUtil;
import com.tasty.reviews.tastyreviews.global.jwt.repository.RefreshRepository;
import com.tasty.reviews.tastyreviews.global.jwt.damain.RefreshEntity;
import com.tasty.reviews.tastyreviews.member.domain.Member;
import com.tasty.reviews.tastyreviews.member.dto.LoginDTO;
import com.tasty.reviews.tastyreviews.member.repository.MemberRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager; // 인증 처리 매니저
    private final JWTUtil jwtUtil; // JWT 토큰 생성 및 검증 유틸리티
    private final RefreshRepository refreshRepository; // 리프레시 토큰 저장소
    private final MemberRepository memberRepository; // 사용자 정보 저장소

    // 사용자가 로그인을 시도할 때 호출되는 메서드
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        LoginDTO loginDTO;
        try {
            ObjectMapper objectMapper = new ObjectMapper(); // JSON 파싱을 위한 ObjectMapper
            ServletInputStream inputStream = request.getInputStream();
            String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8); // 요청 본문을 문자열로 변환
            loginDTO = objectMapper.readValue(messageBody, LoginDTO.class); // 요청 본문을 LoginDTO로 변환
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String username = loginDTO.getEmail();
        String password = loginDTO.getPassword();
        // 스프링 시큐리티에서 username과 password를 검증하기 위한 UsernamePasswordAuthenticationToken 생성
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);
        // 검증을 위한 AuthenticationManager로 토큰 전달
        return authenticationManager.authenticate(authToken);
    }

    // 로그인 성공 시 실행되는 메서드
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException {
        String username = authentication.getName(); // 유저 정보
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();
        // 토큰 생성
        String access = jwtUtil.createJwt("access", username, role, 600000L); // 엑세스 토큰 생성
        String refresh = jwtUtil.createJwt("refresh", username, role, 86400000L); // 리프레시 토큰 생성
        // 서버에 리프레시 토큰 저장
        addRefreshEntity(username, refresh, 86400000L);
        ObjectMapper mapper = new ObjectMapper();
        String nickname = findNickname(username); // DB에서 닉네임 가져오기
        Long id = findId(username);
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("email", username);
        responseData.put("role", role);
        responseData.put("nickname", nickname);
        responseData.put("id", id);
        response.setHeader("Authorization", access); // Authorization 헤더에 엑세스 토큰 저장
        response.addCookie(createCookie("refresh", refresh)); // 쿠키에 리프레시 토큰 저장
        response.setContentType("application/json;charset=UTF-8"); // JSON 응답/한글 인코딩
        response.setStatus(HttpStatus.OK.value()); // 상태코드 설정
        mapper.writeValue(response.getWriter(), responseData); // 응답 본문
    }

    // 로그인 실패 시 실행되는 메서드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        response.setStatus(401); // 로그인 실패 시 401 응답 코드 반환
    }

    // 쿠키 생성 메서드
    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24 * 60 * 60); // 쿠키 유효 기간 설정
        // cookie.setSecure(true);
        // cookie.setPath("/");
        cookie.setHttpOnly(false); // 쿠키 HttpOnly 속성 설정
        return cookie;
    }

    // 리프레시 토큰을 데이터베이스에 저장하는 메서드
    private void addRefreshEntity(String userEmail, String refresh, Long expiredMs) {
        Date date = new Date(System.currentTimeMillis() + expiredMs); // 만료 날짜 계산
        RefreshEntity refreshEntity = new RefreshEntity();
        refreshEntity.setUserEmail(userEmail);
        refreshEntity.setRefresh(refresh);
        refreshEntity.setExpiration(date.toString());
        refreshRepository.save(refreshEntity); // 리프레시 토큰 저장
    }

    // 이메일로 사용자 닉네임 조회
    private String findNickname(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("일치하는 이메일이 없습니다"));
        return member.getNickname();
    }

    // 이메일로 사용자 ID 조회
    private Long findId(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("일치하는 이메일이 없습니다"));
        return member.getId();
    }
}
