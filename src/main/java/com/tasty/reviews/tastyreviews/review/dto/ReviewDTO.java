package com.tasty.reviews.tastyreviews.review.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDTO {

    private Long id;
    private Long memberId;
    private Long restaurantId;
    private int rating;
    private String comment;
    private String imageURL;

}
