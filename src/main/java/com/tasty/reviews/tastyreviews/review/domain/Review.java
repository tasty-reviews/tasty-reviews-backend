package com.tasty.reviews.tastyreviews.review.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tasty.reviews.tastyreviews.global.common.BaseTimeEntity;
import com.tasty.reviews.tastyreviews.member.domain.Member;
import com.tasty.reviews.tastyreviews.restaruant.domain.Restaurant;
import com.tasty.reviews.tastyreviews.upload.domain.UploadedFile;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

/**
 * 리뷰 정보를 나타내는 엔티티 클래스
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Review extends BaseTimeEntity {

    /**
     * 리뷰 식별자
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Review_id")
    private Long id;

    /**
     * 리뷰를 작성한 회원 정보
     */
    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "Member_id", nullable = false)
    private Member member;

    /**
     * 리뷰가 작성된 레스토랑 정보
     */
    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "Restaurant_id", nullable = false)
    private Restaurant restaurant;

    /**
     * 리뷰 평점 (1부터 5까지의 범위)
     */
    @Column(name = "Rating", nullable = false)
    @Min(1)
    @Max(5)
    private int rating;

    /**
     * 리뷰 내용
     */
    @Column(nullable = false)
    private String comment;

    /**
     * 리뷰에 첨부된 이미지 파일 리스트
     * 양방향 연관 관계 설정: Review 엔티티에서 UploadedFile 엔티티와의 일대다 관계
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<UploadedFile> images;
}
