package com.tasty.reviews.tastyreviews.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tasty.reviews.tastyreviews.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
public class Restaurant extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Restaurant_Id")
    private Long id;

    @Column(nullable = false)
    private String placeName;

    @Column(nullable = false)
    private String categoryName;

//    @Column(nullable = false)
    private String phone;

    private String imageUrl;

    @Column(nullable = false)
    private Integer viewCount;

    @Column(nullable = false)
    private String roadAddressName;

    @Column(nullable = false)
    private String x;

    @Column(nullable = false)
    private String y;

//    @Column(nullable = false)
    private String placeUrl;
//
    // 여러 개의 리뷰를 저장하기 위한 List
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();

//    @JsonIgnore
//    @ManyToOne
//    @JoinColumn(name = "UserMap_id", nullable = false)
//    private UserMap userMap;
}
