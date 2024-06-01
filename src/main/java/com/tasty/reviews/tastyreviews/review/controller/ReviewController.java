package com.tasty.reviews.tastyreviews.review.controller;

import com.tasty.reviews.tastyreviews.review.domain.Review;
import com.tasty.reviews.tastyreviews.review.dto.ReviewResponseDTO;
import com.tasty.reviews.tastyreviews.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    @PostMapping("/review/add/{restaurantId}")
    public ResponseEntity<ReviewResponseDTO> addReviewToRestaurant(@PathVariable(name = "restaurantId") Long restaurantId,
                                                                   @RequestParam("comment") String comment,
                                                                   @RequestParam("rating") int rating,
                                                                   @RequestParam(required = false, name = "files") List<MultipartFile> files) {
        ReviewResponseDTO savedReview = reviewService.createReview(restaurantId, comment, rating, files);
        reviewService.restaurantAvgRatingUpdate(savedReview.getRestaurantId());//음식점 별점 반영

        return ResponseEntity.status(HttpStatus.CREATED).body(savedReview);
    }

    // 리뷰 수정
    @PutMapping("/reviews/{reviewId}")
    public ResponseEntity<ReviewResponseDTO> updateReview(@PathVariable Long reviewId,
                                               @RequestParam("comment") String comment,
                                               @RequestParam("rating") int rating,
                                               @RequestParam(value = "files", required = false) List<MultipartFile> files) throws IOException {
        ReviewResponseDTO updatedReview = reviewService.updateReview(reviewId, comment, rating, files);
        return ResponseEntity.ok(updatedReview);
    }

    // 리뷰 삭제
    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }

}