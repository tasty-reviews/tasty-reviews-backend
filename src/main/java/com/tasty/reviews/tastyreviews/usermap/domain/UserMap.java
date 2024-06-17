package com.tasty.reviews.tastyreviews.usermap.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tasty.reviews.tastyreviews.member.domain.Member;
import com.tasty.reviews.tastyreviews.restaruant.domain.Restaurant;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity // JPA 엔티티로 지정
@Getter // Lombok을 사용하여 Getter 자동 생성
@Setter // Lombok을 사용하여 Setter 자동 생성
@NoArgsConstructor // 기본 생성자 자동 생성
@AllArgsConstructor // 모든 필드를 포함하는 생성자 자동 생성
@Builder // 빌더 패턴을 통한 객체 생성 지원
public class UserMap {

    @Id // 기본 키로 설정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 데이터베이스에 의해 자동 생성되는 기본 키 설정
    @Column(name = "UserMap_Id") // 데이터베이스에서 사용될 컬럼 이름 지정
    private Long id; // 내지도 ID

    @Column(nullable = false) // null을 허용하지 않는 컬럼
    private String name; // 내지도 이름

    @Column(nullable = false) // null을 허용하지 않는 컬럼
    private String description; // 내지도 설명

    private String myMapImage; // 내지도 이미지 파일 경로

    private int viewCount; // 내지도 조회 횟수

    @ManyToMany // 다대다 관계 설정
    @JoinTable( // 연결 테이블 설정
            name = "usermap_restaurant", // 연결 테이블 이름
            joinColumns = @JoinColumn(name = "usermap_id"), // UserMap 엔티티의 외래 키
            inverseJoinColumns = @JoinColumn(name = "restaurant_id") // Restaurant 엔티티의 외래 키
    )
    private List<Restaurant> restaurants = new ArrayList<>(); // 내지도에 등록된 음식점 목록

    @JsonIgnore // JSON 직렬화에서 해당 필드를 무시
    @ManyToOne(fetch = LAZY) // 다대일 관계 설정 (지연 로딩)
    @JoinColumn(name = "Member_id", nullable = false) // 외래 키 설정
    private Member member; // 내지도를 작성한 회원 정보

}
