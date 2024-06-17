package com.tasty.reviews.tastyreviews.restaruant.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tasty.reviews.tastyreviews.global.common.BaseTimeEntity;
import com.tasty.reviews.tastyreviews.review.domain.Review;
import com.tasty.reviews.tastyreviews.usermap.domain.UserMap;
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
    private String placeId;

    @Column(nullable = false)
    private String categoryName;

    @Column(nullable = false)
    private int viewCount;

    @Column(nullable = false)
    private int reviewCount;

    @Column(nullable = false)
    private String roadAddressName;

    @Column(nullable = false)
    private String x;

    @Column(nullable = false)
    private String y;

    private String avgRating;

    private String phone;

    private String imageUrl;

    private String placeUrl;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();

    @JsonIgnore
    @ManyToMany(mappedBy = "restaurants")
    private List<UserMap> userMaps = new ArrayList<>();

    /**
     * 리뷰를 추가하고, 해당 리뷰의 음식점을 현재 음식점으로 설정합니다.
     *
     * @param review 추가할 리뷰
     */
    public void addReview(Review review) {
        reviews.add(review);
        review.setRestaurant(this); // Review 엔티티의 restaurant 필드 설정
    }

    /**
     * 음식점의 리뷰 목록을 설정하고, 각 리뷰의 음식점을 현재 음식점으로 설정합니다.
     *
     * @param reviews 설정할 리뷰 목록
     */
    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
        for (Review review : reviews) {
            review.setRestaurant(this); // 각 Review 엔티티의 restaurant 필드 설정
        }
    }

    /**
     * 음식점의 평균 평점을 업데이트합니다.
     *
     * @param totalRating 총 평점 합계
     * @param reviewCount 리뷰 개수
     */
    public void updateRating(int totalRating, int reviewCount) {
        double avg = (double) totalRating / reviewCount;
        String stringAvg = String.format("%.2f", avg);
        this.avgRating = stringAvg; // 평균 평점 문자열로 설정
    }
}
