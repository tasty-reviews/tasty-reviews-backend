package com.tasty.reviews.tastyreviews.service;


import com.tasty.reviews.tastyreviews.domain.Review;
import com.tasty.reviews.tastyreviews.dto.ReviewDTO;
import com.tasty.reviews.tastyreviews.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private

    // 특정 레스토랑의 리뷰 조회
    public List<Review> getReviewsByRestaurantId(Long restaurantId) {
        return reviewRepository.findByRestaurantId(restaurantId);
    }

    // 특정 회원의 리뷰 조회
    public List<Review> getReviewsByMemberId(Long memberId) {
        return reviewRepository.findByMemberId(memberId);
    }

    // 리뷰 생성
    public Review createReview(Review review) {
        return reviewRepository.save(review);
    }

    public Review updateReview(Long reviewId, Review updatedReview) {
        Review existingReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found with id: " + reviewId));

        existingReview.setRating(updatedReview.getRating());
        existingReview.setComment(updatedReview.getComment());
        existingReview.setImageURL(updatedReview.getImageURL());

        return reviewRepository.save(existingReview);
    }

    // 리뷰 삭제
    public void deleteReview(Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }

}
