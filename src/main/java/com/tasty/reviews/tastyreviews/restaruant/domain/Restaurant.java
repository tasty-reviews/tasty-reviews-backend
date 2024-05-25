package com.tasty.reviews.tastyreviews.restaruant.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tasty.reviews.tastyreviews.global.common.BaseTimeEntity;
import com.tasty.reviews.tastyreviews.review.domain.Review;
import com.tasty.reviews.tastyreviews.usermap.domain.UserMap;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
public class Restaurant extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Restaurant_Id")
    private Long id;

    @Column(nullable = false)
    private String placeName;

    @Column(nullable = false)
    private String categoryName;

    @Column(nullable = false)
    private Integer viewCount;

    @Column(nullable = false)
    private Integer reviewCount;

    @Column(nullable = false)
    private String roadAddressName;

    @Column(nullable = false)
    private String x;

    @Column(nullable = false)
    private String y;

    private String phone;
    private String imageUrl;
    private String placeUrl;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();

    @JsonIgnore
    @ManyToMany(mappedBy = "restaurants")
    private List<UserMap> userMaps = new ArrayList<>();

    public void addReview(Review review) {
        reviews.add(review);
        review.setRestaurant(this);
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
        for (Review review : reviews) {
            review.setRestaurant(this);
        }
    }

}
