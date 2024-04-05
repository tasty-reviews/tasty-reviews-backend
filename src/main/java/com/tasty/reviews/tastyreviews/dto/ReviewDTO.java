package com.tasty.reviews.tastyreviews.dto;

import com.tasty.reviews.tastyreviews.domain.Member;
import com.tasty.reviews.tastyreviews.domain.Restaurant;
import com.tasty.reviews.tastyreviews.domain.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ReviewDTO {

    private String comment;
    private int rating;
    private String imageURL;
    private Long restaurantId;
    private Long memberId;

    public Review toEntity(Member member, Restaurant restaurant) {
        return Review.builder()
                .comment(this.comment)
                .rating(this.rating)
                .member(member)
                .restaurant(restaurant)
                .build();
    }
}
