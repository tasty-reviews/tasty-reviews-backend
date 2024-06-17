package com.tasty.reviews.tastyreviews.review.dto;

import com.tasty.reviews.tastyreviews.upload.domain.UploadedFile;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * 특정 회원의 리뷰 정보를 반환하는 DTO 클래스
 */
@Getter
@Setter
@NoArgsConstructor
public class MemerReviewReadResponseDTO {

    /**
     * 리뷰 식별자
     */
    private Long id;

    /**
     * 회원 식별자
     */
    private Long memberId;

    /**
     * 회원 닉네임
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
     * 리뷰가 작성된 음식점 ID
     */
    private String placeId;

    /**
     * 리뷰가 작성된 음식점 이름
     */
    private String placeName;

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
     * 생성자(builder pattern)
     */
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
