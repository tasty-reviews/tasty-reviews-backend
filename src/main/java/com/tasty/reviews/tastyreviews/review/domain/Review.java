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

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Review extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Review_id")
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "Member_id", nullable = false)
    private Member member;

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "Restaurant_id", nullable = false)
    private Restaurant restaurant;

    @Column(name = "Rating", nullable = false)
    @Min(1)
    @Max(5)
    private int rating;

    @Column(nullable = false)
    private String comment;

    // Review 엔티티와 UploadedFile 엔티티 간의 양방향 관계를 맺기 위해 추가된 필드
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL)
    private List<UploadedFile> images;


}
