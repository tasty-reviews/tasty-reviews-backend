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
    private String name;
    private String category;
    private String description;
    private String imageUrl;
    private Integer viewCount;
    private String address;

    // 엔티티로 변환하는 메서드
    public Restaurant toEntity() {
        return Restaurant.builder()
                .id(this.id)
                .name(this.name)
                .category(this.category)
                .description(this.description)
                .imageurl(this.imageUrl)
                .viewcount(this.viewCount)
                .address(this.address)
                .build();
    }

    // 정적 팩토리 메서드를 이용한 DTO 생성
    public static RestaurantDTO fromEntity(Restaurant restaurant) {
        return RestaurantDTO.builder()
                .id(restaurant.getId())
                .name(restaurant.getName())
                .category(restaurant.getCategory())
                .description(restaurant.getDescription())
                .imageUrl(restaurant.getImageurl())
                .viewCount(restaurant.getViewcount())
                .address(restaurant.getAddress())
                .build();
    }
}
