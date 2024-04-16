package com.tasty.reviews.tastyreviews.service;

import com.tasty.reviews.tastyreviews.domain.Restaurant;
import com.tasty.reviews.tastyreviews.domain.Review;
import com.tasty.reviews.tastyreviews.domain.Member; // 추가
import com.tasty.reviews.tastyreviews.repository.RestaurantRepository;
import com.tasty.reviews.tastyreviews.repository.ReviewRepository;
import com.tasty.reviews.tastyreviews.repository.MemberRepository; // 추가
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
    private final MemberRepository memberRepository; // 추가

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

        // 사용자가 로그인되어 있는지 확인
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("User must be logged in to create a review");
        }

        // 사용자의 멤버ID를 추출
        String email = authentication.getName();

        // 사용자 정보 가져오기
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("User not found"));

        // 식당 정보 가져오기
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid restaurant ID: " + restaurantId));

        // 리뷰에 식당 및 정보 설정
        review.setRestaurant(restaurant);
        review.setMember(member);

        // 리뷰 저장
        return reviewRepository.save(review);
    }

    //리뷰 수정
    @Transactional
    public Review updateReview(Long reviewId, Review updatedReview) {
        // 사용자의 인증 정보를 가져옴
        isLogined();

        // 리뷰 ID를 가진 리뷰 엔티티를 조회
        Review existingReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid review ID: " + reviewId));

        // 업데이트할 내용으로 기존 리뷰를 갱신
        existingReview.setRating(updatedReview.getRating());
        existingReview.setComment(updatedReview.getComment());
        existingReview.setImageURL(updatedReview.getImageURL());

        // 갱신된 리뷰 저장 및 반환
        return reviewRepository.save(existingReview);
    }

    //리뷰 삭제
    @Transactional
    public void deleteReview(Long reviewId) {
        isLogined();

        // 리뷰 ID를 가진 리뷰 엔티티를 조회
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid review ID: " + reviewId));

        reviewRepository.delete(review);
    }

    private static void isLogined() {
        // 사용자의 인증 정보를 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 사용자가 로그인되어 있는지 확인
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("User must be logged in to create a review");
        }
    }

}
