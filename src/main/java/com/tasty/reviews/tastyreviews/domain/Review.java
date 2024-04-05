package com.tasty.reviews.tastyreviews.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Review_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "Member_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "Restaurant_id", nullable = false)
    private Restaurant restaurant;

    @Column(name = "Rating", nullable = false)
    @Min(1)
    @Max(5)
    private int rating;

    @Column(nullable = false)
    private String comment;

    private String imageURL;
}
