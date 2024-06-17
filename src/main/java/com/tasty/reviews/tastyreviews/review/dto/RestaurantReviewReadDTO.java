package com.tasty.reviews.tastyreviews.review.dto;

import com.tasty.reviews.tastyreviews.upload.domain.UploadedFile;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * 특정 음식점의 리뷰 정보를 반환하는 DTO 클래스
 */
@Getter
@Setter
@NoArgsConstructor
public class RestaurantReviewReadDTO {

    /**
     * 리뷰 식별자
     */
    private Long id;

    /**
     * 리뷰 생성일시
     */
    private String createdDate;

    /**
     * 리뷰 수정일시
     */
    private String modifiedDate;

    /**
     * 리뷰 평점
     */
    private int rating;

    /**
     * 리뷰를 작성한 회원의 닉네임
     */
    private String nickname;

    /**
     * 리뷰 내용
     */
    private String comment;

    /**
     * 리뷰에 첨부된 이미지 파일 리스트
     */
    private List<UploadedFile> images;

    /**
     * 생성자(builder pattern)
     */
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
