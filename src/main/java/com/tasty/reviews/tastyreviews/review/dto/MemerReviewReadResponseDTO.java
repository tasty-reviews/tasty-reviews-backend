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
public class MemerReviewReadResponseDTO {

    private Long id;
    private Long memberId;
    private String nickname;
    private String createdDate;
    private String modifiedDate;
    private String placeId;
    private String placeName;
    private int rating;
    private String comment;
    private List<UploadedFile> images;

    @Builder
    public MemerReviewReadResponseDTO(String comment, String createdDate, Long id, List<UploadedFile> images, Long memberId, String modifiedDate, String nickname, String placeId, String placeName, int rating) {
        this.comment = comment;
        this.createdDate = createdDate;
        this.id = id;
        this.images = images;
        this.memberId = memberId;
        this.modifiedDate = modifiedDate;
        this.nickname = nickname;
        this.placeId = placeId;
        this.placeName = placeName;
        this.rating = rating;
    }
}
