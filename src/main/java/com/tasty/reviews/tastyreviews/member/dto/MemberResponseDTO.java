package com.tasty.reviews.tastyreviews.member.dto;

import com.tasty.reviews.tastyreviews.review.domain.Review;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MemberResponseDTO {

    private Long id; // 회원 ID
    private String nickname; // 회원 닉네임
    private String email; // 회원 이메일
    private List<Review> reviewList; // 회원이 작성한 리뷰 목록

    @Builder
    public MemberResponseDTO(Long id, String email, String nickname, List<Review> reviewList) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.reviewList = reviewList;
    }
}
