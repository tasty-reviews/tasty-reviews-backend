package com.tasty.reviews.tastyreviews.review.repository;

import com.tasty.reviews.tastyreviews.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByRestaurantId(Long restaurantId);

    List<Review> findByMemberId(Long memberId);
}