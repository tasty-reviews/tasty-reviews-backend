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
    private int resturanstCount;

    @Builder
    public AllUserMapResponseDTO(Long id, String myMapImage, String nickname, int resturanstCount, String name) {
        this.id = id;
        this.myMapImage = myMapImage;
        this.nickname = nickname;
        this.resturanstCount = resturanstCount;
        this.name = name;
    }
}
