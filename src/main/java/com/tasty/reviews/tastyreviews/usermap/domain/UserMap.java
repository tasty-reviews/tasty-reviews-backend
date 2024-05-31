package com.tasty.reviews.tastyreviews.usermap.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tasty.reviews.tastyreviews.member.domain.Member;
import com.tasty.reviews.tastyreviews.restaruant.domain.Restaurant;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
public class UserMap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UserMap_Id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    private String myMapImage;

    @ManyToMany
    @JoinTable(
            name = "usermap_restaurant", // 연결 테이블 이름
            joinColumns = @JoinColumn(name = "usermap_id"), // UserMap 쪽 참조 키
            inverseJoinColumns = @JoinColumn(name = "restaurant_id") // Restaurant 쪽 참조 키
    )
    private List<Restaurant> restaurants = new ArrayList<>();

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "Member_id", nullable = false)
    private Member member;

}
