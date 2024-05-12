package com.tasty.reviews.tastyreviews.dto;

import com.tasty.reviews.tastyreviews.domain.Restaurant;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantDTO {
    private Long id;
    private String placeName;
    private String categoryName;
    private String imageUrl;
    private Integer viewCount;
    private String roadAddressname;
    private String phone;
    private String placeUrl;
    private String x;
    private String y;


    // 엔티티로 변환하는 메서드
    public Restaurant toEntity() {
        return Restaurant.builder()
                .id(this.id)
                .placeName(this.placeName)
                .categoryName(this.categoryName)
                .imageUrl(this.imageUrl)
                .viewCount(this.viewCount)
                .roadAddressName(this.roadAddressname)
                .phone(this.phone)
                .placeUrl(this.placeUrl)
                .x(this.x)
                .y(this.y)
                .build();
    }


    // 정적 팩토리 메서드를 이용한 DTO 생성
    public static RestaurantDTO fromEntity(Restaurant restaurant) {
        return RestaurantDTO.builder()
                .id(restaurant.getId())
                .placeName(restaurant.getPlaceName())
                .categoryName(restaurant.getCategoryName())
                .imageUrl(restaurant.getImageUrl())
                .viewCount(restaurant.getViewCount())
                .roadAddressname(restaurant.getRoadAddressName())
                .phone(restaurant.getPhone())
                .placeUrl(restaurant.getPlaceUrl())
                .x(restaurant.getX())
                .y(restaurant.getY())
                .build();
    }
}
