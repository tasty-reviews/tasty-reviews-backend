package com.tasty.reviews.tastyreviews.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 45)
    private String email;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false)
    private int age;

    @Column(nullable = false, unique = true, length = 45)
    private String nickname;

    @Column(nullable = false)
    private String gender;

    @Enumerated(EnumType.STRING)
    private Role role;

    //하나의 회원에 여러개의 리뷰를 저장
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();

    //하나의 회원에 여러개의 사용자지도를 저장
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<UserMap> usermaps = new ArrayList<>();

    //비즈니스 로직
    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }
}