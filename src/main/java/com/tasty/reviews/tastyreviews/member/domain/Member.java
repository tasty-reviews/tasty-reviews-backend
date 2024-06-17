package com.tasty.reviews.tastyreviews.member.domain;

import com.tasty.reviews.tastyreviews.global.common.BaseTimeEntity;
import com.tasty.reviews.tastyreviews.review.domain.Review;
import com.tasty.reviews.tastyreviews.usermap.domain.UserMap;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 매개변수 없는 생성자 생성 방지
@AllArgsConstructor // 모든 필드를 매개변수로 받는 생성자 생성
@Builder // 빌더 패턴을 사용할 수 있게 함
public class Member extends BaseTimeEntity { // BaseTimeEntity를 상속받아 생성 및 수정 시간 자동 관리

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 회원 식별자

    @Column(nullable = false, unique = true, length = 45)
    private String email; // 회원 이메일 (유일하고 필수)

    @Column(nullable = false, length = 100)
    private String password; // 회원 비밀번호 (필수)

    @Column(nullable = false)
    private int age; // 회원 나이 (필수)

    @Column(nullable = false, unique = true, length = 45)
    private String nickname; // 회원 닉네임 (유일하고 필수)

    @Column(nullable = false)
    private String gender; // 회원 성별 (필수)

    @Enumerated(EnumType.STRING)
    private Role role; // 회원 권한 (사용자, 관리자 등)

    // 하나의 회원에 여러 개의 리뷰를 저장할 리스트
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();

    // 하나의 회원에 여러 개의 사용자지도를 저장할 리스트
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<UserMap> usermaps = new ArrayList<>();

    /* 비즈니스 로직 */

    // 회원의 닉네임을 업데이트하는 메서드
    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    // 회원의 비밀번호를 업데이트하는 메서드
    public void updatePassword(String password) {
        this.password = password;
    }
}
