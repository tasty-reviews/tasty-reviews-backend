package com.tasty.reviews.tastyreviews.usermap.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserMapResponseDTO {

    private Long id;
    private String name;
    private String description;
    private String myMapImage;
    private String nickname;
    private int viewCount;


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
