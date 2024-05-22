package com.tasty.reviews.tastyreviews.review.controller;

import com.tasty.reviews.tastyreviews.review.domain.Review;
import com.tasty.reviews.tastyreviews.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReviewController {

    private final ReviewService reviewService;

    // 특정 레스토랑의 리뷰 조회
    @GetMapping("/restaurants/{restaurantId}")
    public ResponseEntity<List<Review>> getReviewsByRestaurantId(@PathVariable(name = "restaurantId") Long restaurantId) {
        List<Review> reviews = reviewService.getReviewsByRestaurantId(restaurantId);
        return ResponseEntity.ok(reviews);
    }

    // 특정 회원의 리뷰 조회
    @GetMapping("/members/reviews")
    public ResponseEntity<List<Review>> getReviewsByMemberId() {
        List<Review> reviews = reviewService.getReviewsByMemberId();
        return ResponseEntity.ok(reviews);
    }

    // 리뷰 생성
    @PostMapping("/reviews/{restaurantId}")
    public ResponseEntity<Review> addReviewToRestaurant(@PathVariable(name = "restaurantId") Long restaurantId,
                                                        @RequestBody Review review)
    {
        // 특정 식당에 리뷰 추가
        Review savedReview = reviewService.createReview(restaurantId, review);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedReview);
    }

    // 리뷰 수정
    @PutMapping("/reviews/{reviewId}")
    public ResponseEntity<Review> updateReview(@PathVariable Long reviewId, @RequestBody Review review) {
        Review updatedReview = reviewService.updateReview(reviewId, review);
        return ResponseEntity.ok(updatedReview);
    }

    // 리뷰 삭제
    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }

}