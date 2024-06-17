package com.tasty.reviews.tastyreviews.review.dto;

import com.tasty.reviews.tastyreviews.review.domain.Review;
import com.tasty.reviews.tastyreviews.upload.domain.UploadedFile;
import lombok.*;

import java.util.List;

/**
 * 리뷰 생성 시 클라이언트에게 반환할 응답 DTO 클래스
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewResponseDTO {

    /**
     * 리뷰 식별자
     */
    private Long id;

    /**
     * 리뷰를 작성한 회원의 식별자
     */
    private Long memberId;

    /**
     * 리뷰를 작성한 회원의 닉네임
     */
    private String nickname;

    /**
     * 리뷰 생성일시
     */
    private String createdDate;

    /**
     * 리뷰 수정일시
     */
    private String modifiedDate;

    /**
     * 리뷰가 작성된 음식점의 식별자
     */
    private Long restaurantId;

    /**
     * 리뷰 평점
     */
    private int rating;

    /**
     * 리뷰 내용
     */
    private String comment;

    /**
     * 리뷰에 첨부된 이미지 파일 리스트
     */
    private List<UploadedFile> images;

    /**
     * Review 엔티티를 기반으로 한 생성자
     */
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
