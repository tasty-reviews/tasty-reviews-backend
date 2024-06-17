package com.tasty.reviews.tastyreviews.restaruant.dto;

import com.tasty.reviews.tastyreviews.restaruant.domain.Restaurant;
import com.tasty.reviews.tastyreviews.review.domain.Review;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantDTO {
    private Long id; // 음식점 식별자
    private String placeName; // 음식점 이름
    private String categoryName; // 음식점 카테고리 이름
    private String imageUrl; // 음식점 이미지 URL
    private int viewCount; // 음식점 조회수
    private int reviewCount; // 음식점 리뷰 개수
    private String avgRating; // 음식점 평균 평점
    private String roadAddressname; // 도로명 주소
    private String phone; // 전화번호
    private String placeUrl; // 음식점 URL
    private String placeId; // 음식점 ID
    private String x; // 음식점 경도
    private String y; // 음식점 위도
    private List<Review> reviews; // 음식점 리뷰 목록

    /**
     * DTO에서 엔티티로 변환하는 메서드입니다.
     *
     * @return 변환된 Restaurant 엔티티
     */
    public Restaurant toEntity() {
        return Restaurant.builder()
                .id(this.id)
                .placeName(this.placeName)
                .categoryName(this.categoryName)
                .imageUrl(this.imageUrl)
                .viewCount(this.viewCount)
                .reviewCount(this.reviewCount)
                .avgRating(this.avgRating)
                .roadAddressName(this.roadAddressname)
                .phone(this.phone)
                .placeUrl(this.placeUrl)
                .placeId(this.placeId)
                .x(this.x)
                .y(this.y)
                .reviews(this.reviews) // 리뷰 목록 포함
                .build();
    }

    /**
     * Restaurant 엔티티에서 RestaurantDTO로 변환하는 정적 팩토리 메서드입니다.
     *
     * @param restaurant 변환할 Restaurant 엔티티
     * @return 변환된 RestaurantDTO 객체
     */
    public static RestaurantDTO fromEntity(Restaurant restaurant) {
        return RestaurantDTO.builder()
                .id(restaurant.getId())
                .placeName(restaurant.getPlaceName())
                .categoryName(restaurant.getCategoryName())
                .imageUrl(restaurant.getImageUrl())
                .viewCount(restaurant.getViewCount())
                .reviewCount(restaurant.getReviewCount())
                .avgRating(restaurant.getAvgRating())
                .roadAddressname(restaurant.getRoadAddressName())
                .phone(restaurant.getPhone())
                .placeUrl(restaurant.getPlaceUrl())
                .placeId(restaurant.getPlaceId())
                .x(restaurant.getX())
                .y(restaurant.getY())
                .reviews(restaurant.getReviews()) // 리뷰 목록 포함
                .build();
    }
}
