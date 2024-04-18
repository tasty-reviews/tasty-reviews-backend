package com.tasty.reviews.tastyreviews.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

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
    @ManyToOne
    @JoinColumn(name = "Member_id", nullable = false)
    private Member member;

    @JsonIgnore
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
