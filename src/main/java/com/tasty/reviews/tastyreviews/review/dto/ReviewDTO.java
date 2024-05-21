package com.tasty.reviews.tastyreviews.review.dto;

import com.tasty.reviews.tastyreviews.member.domain.Member;
import com.tasty.reviews.tastyreviews.restaruant.domain.Restaurant;
import com.tasty.reviews.tastyreviews.review.domain.Review;
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

    public Review toEntity() {
        return Review.builder()
                .id(this.id)
                .member(Member.builder().id(this.memberId).build())
                .restaurant(Restaurant.builder().id(this.restaurantId).build())
                .rating(this.rating)
                .comment(this.comment)
                .imageURL(this.imageURL)
                .build();
    }

    public static ReviewDTO fromEntity(Review review) {
        return ReviewDTO.builder()
                .id(review.getId())
                .memberId(review.getMember().getId())
                .restaurantId(review.getRestaurant().getId())
                .rating(review.getRating())
                .comment(review.getComment())
                .imageURL(review.getImageURL())
                .build();
    }
}
