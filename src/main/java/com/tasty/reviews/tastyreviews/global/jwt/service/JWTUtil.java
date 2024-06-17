package com.tasty.reviews.tastyreviews.global.jwt.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * JWT 생성 및 검증을 담당하는 유틸리티 클래스입니다.
 * application.yml에서 설정된 secret 값을 기반으로 JWT를 생성하고 검증합니다.
 */
@Component
public class JWTUtil {

    private final Key key;

    /**
     * JWTUtil 클래스의 생성자입니다.
     * application.yml에서 설정된 secret 값을 BASE64 디코딩하여 바이트 배열로 변환한 후,
     * HMAC 알고리즘의 키로 사용합니다.
     *
     * @param secret application.yml에서 설정된 JWT secret 값
     */
    public JWTUtil(@Value("${spring.jwt.secret}") String secret) {
        byte[] byteSecretKey = Decoders.BASE64.decode(secret);
        key = Keys.hmacShaKeyFor(byteSecretKey);
    }

    /**
     * 주어진 토큰에서 "category" 클레임을 추출하여 반환합니다.
     *
     * @param token 추출할 JWT 토큰
     * @return 토큰의 카테고리 정보
     */
    public String getCategory(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().get("category", String.class);
    }

    /**
     * 주어진 토큰에서 "username" 클레임을 추출하여 반환합니다.
     *
     * @param token 추출할 JWT 토큰
     * @return 토큰의 사용자 아이디 정보
     */
    public String getUsername(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().get("username", String.class);
    }

    /**
     * 주어진 토큰에서 "role" 클레임을 추출하여 반환합니다.
     *
     * @param token 추출할 JWT 토큰
     * @return 토큰의 사용자 권한 정보
     */
    public String getRole(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().get("role", String.class);
    }

    /**
     * 주어진 토큰의 만료 여부를 확인합니다.
     *
     * @param token 확인할 JWT 토큰
     * @return 토큰의 만료 여부 (true: 만료됨, false: 만료되지 않음)
     */
    public Boolean isExpired(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getExpiration().before(new Date());
    }

    /**
     * 주어진 사용자 아이디, 권한 및 만료 시간(ms)을 사용하여 JWT를 생성합니다.
     *
     * @param category JWT의 카테고리 정보
     * @param username JWT의 사용자 아이디 정보
     * @param role JWT의 사용자 권한 정보
     * @param expiredMs JWT의 만료 시간 (밀리초)
     * @return 생성된 JWT
     */
    public String createJwt(String category, String username, String role, Long expiredMs) {
        // 클레임(claim) 객체 생성
        Claims claims = Jwts.claims();
        claims.put("category", category); // 토큰 카테고리 설정
        claims.put("username", username); // 사용자 아이디 설정
        claims.put("role", role); // 사용자 권한 설정

        // JWT 빌더를 사용하여 토큰 생성
        return Jwts.builder()
                .setClaims(claims) // 클레임 설정
                .setIssuedAt(new Date(System.currentTimeMillis())) // 발행 시간 설정
                .setExpiration(new Date(System.currentTimeMillis() + expiredMs)) // 만료 시간 설정
                .signWith(key, SignatureAlgorithm.HS256) // 서명 알고리즘 및 키 설정
                .compact(); // 토큰 생성 및 반환
    }
}
