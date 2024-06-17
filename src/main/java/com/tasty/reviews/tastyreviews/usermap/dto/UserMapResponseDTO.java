package com.tasty.reviews.tastyreviews.usermap.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserMapResponseDTO {

    private Long id; // 내지도 ID
    private String name; // 내지도 이름
    private String description; // 내지도 설명
    private String myMapImage; // 내지도 이미지 파일 경로
    private String nickname; // 회원 닉네임
    private int viewCount; // 조회 횟수

    @Builder
    public UserMapResponseDTO(String description, Long id, String myMapImage, String name, String nickname, int viewCount) {
        this.description = description;
        this.id = id;
        this.myMapImage = myMapImage;
        this.name = name;
        this.nickname = nickname;
        this.viewCount = viewCount;
    }
}
