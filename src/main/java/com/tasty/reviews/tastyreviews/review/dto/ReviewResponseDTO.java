package com.tasty.reviews.tastyreviews.review.dto;

import com.tasty.reviews.tastyreviews.review.domain.Review;
import com.tasty.reviews.tastyreviews.upload.domain.UploadedFile;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewResponseDTO {

    private Long id;
    private Long memberId;
    private String nickname;
    private String createdDate;
    private String modifiedDate;
    private Long restaurantId;
    private int rating;
    private String comment;
    private List<UploadedFile> images;

    public ReviewResponseDTO(Review review) {
            this.id = review.getId();
            this.memberId = review.getMember().getId();
            this.nickname = review.getMember().getNickname();
            this.comment = review.getComment();
            this.createdDate = review.getCreatedDate();
            this.modifiedDate = review.getModifiedDate();
            this.rating = review.getRating();
            this.restaurantId = review.getRestaurant().getId();
            this.images = review.getImages();
        }

}