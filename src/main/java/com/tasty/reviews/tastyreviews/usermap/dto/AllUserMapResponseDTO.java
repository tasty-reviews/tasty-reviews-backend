package com.tasty.reviews.tastyreviews.usermap.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AllUserMapResponseDTO {

    private Long id; // 내지도 ID
    private String name; // 내지도 이름
    private String nickname; // 회원 닉네임
    private String myMapImage; // 내지도 이미지 파일 경로
    private String description; // 내지도 설명
    private int restaurantCount; // 등록된 음식점 수
    private int viewCount; // 조회 횟수

    @Builder
    public AllUserMapResponseDTO(String description, Long id, String myMapImage, String name, String nickname, int restaurantCount, int viewCount) {
        this.description = description;
        this.id = id;
        this.myMapImage = myMapImage;
        this.name = name;
        this.nickname = nickname;
        this.restaurantCount = restaurantCount;
        this.viewCount = viewCount;
    }
}
