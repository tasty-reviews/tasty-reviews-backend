package com.tasty.reviews.tastyreviews.controller;

import com.tasty.reviews.tastyreviews.domain.Review;
import com.tasty.reviews.tastyreviews.dto.ReviewDTO;
import com.tasty.reviews.tastyreviews.service.RestaurantService;
import com.tasty.reviews.tastyreviews.service.ReviewService;
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
    public ResponseEntity<List<Review>> getReviewsByRestaurantId(@PathVariable Long restaurantId) {
        List<Review> reviews = reviewService.getReviewsByRestaurantId(restaurantId);
        return ResponseEntity.ok(reviews);
    }

    // 특정 회원의 리뷰 조회
    @GetMapping("/members/{memberId}")
    public ResponseEntity<List<Review>> getReviewsByMemberId(@PathVariable Long memberId) {
        List<Review> reviews = reviewService.getReviewsByMemberId(memberId);
        return ResponseEntity.ok(reviews);
    }

    // 리뷰 생성
    @PostMapping("/review/add/{restaurantId}")
    public ResponseEntity<Review> addReviewToRestaurant(@PathVariable(name = "restaurantId") Long restaurantId,
                                                        @RequestBody Review review)
    {
        // 특정 식당에 리뷰 추가
        Review savedReview = reviewService.createReview(restaurantId, review);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedReview);
    }
}
