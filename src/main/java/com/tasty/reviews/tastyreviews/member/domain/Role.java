package com.tasty.reviews.tastyreviews.member.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    USER("ROLE_USER"), // 사용자 권한
    ADMIN("ROLE_ADMIN"); // 관리자 권한

    private final String value; // 권한 값

    // 각 enum 상수에 대한 설명
    // USER: 일반 사용자 권한
    // ADMIN: 관리자 권한
}
