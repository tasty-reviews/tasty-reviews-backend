package com.tasty.reviews.tastyreviews.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JWTUtil { //jwt 생성 및 검증 클래스

    private final Key key;

    // JWTUtil 클래스의 생성자. application.yml에서 설정된 secret 값을 받아서 key로 사용함.
    public JWTUtil(@Value("${spring.jwt.secret}") String secret) {
        // secret 값을 BASE64 디코딩하여 바이트 배열로 변환하고, 그 값을 HMAC 알고리즘의 키로 사용함.
        byte[] byteSecretKey = Decoders.BASE64.decode(secret);
        key = Keys.hmacShaKeyFor(byteSecretKey);
    }

    // 토큰에서 사용자 아이디를 추출하여 반환함.
    public String getUsername(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().get("username", String.class);
    }

    // 토큰에서 사용자 권한을 추출하여 반환함.
    public String getRole(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().get("role", String.class);
    }

    // 토큰이 만료되었는지 확인함.
    public Boolean isExpired(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getExpiration().before(new Date());
    }

    // 주어진 사용자 아이디, 권한 및 만료 시간(ms)을 사용하여 JWT 생성함.
    public String createJwt(String username, String role, Long expiredMs) {
        // 클레임(claim) 객체 생성
        Claims claims = Jwts.claims();
        claims.put("username", username); // 사용자 아이디 추가
        claims.put("role", role); // 사용자 권한 추가

        // JWT 빌더를 사용하여 토큰 생성
        return Jwts.builder()
                .setClaims(claims) // 클레임 설정
                .setIssuedAt(new Date(System.currentTimeMillis())) // 발행 시간 설정
                .setExpiration(new Date(System.currentTimeMillis() + expiredMs)) // 만료 시간 설정
                .signWith(key, SignatureAlgorithm.HS256) // 서명 알고리즘 설정
                .compact(); // 토큰 생성 및 반환
    }
}
