package com.tasty.reviews.tastyreviews.global.jwt.damain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity // JPA 엔터티임을 나타냄
@Getter
@Setter
public class RefreshEntity { // 리프레시 토큰을 서버에 저장하는 엔티티

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 엔터티의 고유 식별자

    private String userEmail; // 사용자 이메일
    private String refresh; // 리프레시 토큰 값
    private String expiration; // 토큰 만료 일자

}
