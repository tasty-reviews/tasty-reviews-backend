package com.tasty.reviews.tastyreviews.service;


import com.tasty.reviews.tastyreviews.domain.Restaurant;
import com.tasty.reviews.tastyreviews.domain.Review;
import com.tasty.reviews.tastyreviews.repository.RestaurantRepository;
import com.tasty.reviews.tastyreviews.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final RestaurantRepository restaurantRepository;

    // 특정 레스토랑의 리뷰 조회
    public List<Review> getReviewsByRestaurantId(Long restaurantId) {
        return reviewRepository.findByRestaurantId(restaurantId);
    }

    // 특정 회원의 리뷰 조회
    public List<Review> getReviewsByMemberId(Long memberId) {
        return reviewRepository.findByMemberId(memberId);
    }

    // 리뷰 생성
    @Transactional
    public Review createReview(Long restaurantId, Review review) {
        // 사용자의 인증 정보를 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 사용자의 멤버ID를 추출
        String memberId = authentication.getName();

        // 식당 ID를 가진 식당 엔티티를 조회
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid restaurant ID: " + restaurantId));

        // 리뷰에 식당 정보 설정
        review.setRestaurant(restaurant);
//        review.setMemberId(memberId);

        // 리뷰 저장
        return reviewRepository.save(review);
    }



}
