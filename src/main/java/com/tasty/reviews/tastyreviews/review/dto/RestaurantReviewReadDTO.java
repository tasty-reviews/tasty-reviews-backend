package com.tasty.reviews.tastyreviews.review.dto;

import com.tasty.reviews.tastyreviews.upload.domain.UploadedFile;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RestaurantReviewReadDTO {

    private Long id;
    private String createdDate;
    private String modifiedDate;
    private int rating;
    private String nickname;
    private String comment;
    private List<UploadedFile> images;

    @Builder
    public RestaurantReviewReadDTO(String comment, String createdDate, Long id, List<UploadedFile> images,
                                   String modifiedDate, String nickname, int rating) {
        this.comment = comment;
        this.createdDate = createdDate;
        this.id = id;
        this.images = images;
        this.modifiedDate = modifiedDate;
        this.nickname = nickname;
        this.rating = rating;
    }
}
