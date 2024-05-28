package com.tasty.reviews.tastyreviews.member.dto;

import com.tasty.reviews.tastyreviews.review.domain.Review;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MemberResponseDTO {

    private Long id;
    private String nickname;
    private String email;
    private List<Review> reviewList;

    @Builder
    public MemberResponseDTO(Long id, String email, String nickname, List<Review> reviewList) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.reviewList = reviewList;
    }
}
