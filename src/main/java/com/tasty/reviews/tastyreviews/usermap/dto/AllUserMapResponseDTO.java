package com.tasty.reviews.tastyreviews.usermap.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AllUserMapResponseDTO {

    private Long id;
    private String name;
    private String nickname;
    private String myMapImage;
    private String description;
    private int resturanstCount;
    private int viewCount;

    @Builder
    public AllUserMapResponseDTO(String description, Long id, String myMapImage, String name, String nickname, int resturanstCount, int viewCount) {
        this.description = description;
        this.id = id;
        this.myMapImage = myMapImage;
        this.name = name;
        this.nickname = nickname;
        this.resturanstCount = resturanstCount;
        this.viewCount = viewCount;
    }
}
