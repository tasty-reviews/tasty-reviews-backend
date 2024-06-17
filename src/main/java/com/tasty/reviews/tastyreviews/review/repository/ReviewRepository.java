package com.tasty.reviews.tastyreviews.review.repository;

import com.tasty.reviews.tastyreviews.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    /**
     * 특정 음식점(restaurantId)에 속하는 리뷰들을 조회합니다.
     *
     * @param restaurantId 음식점 식별자
     * @return 해당 음식점에 속하는 모든 리뷰들의 리스트
     */
    List<Review> findByRestaurantId(Long restaurantId);

    /**
     * 특정 회원(memberId)이 작성한 리뷰들을 조회합니다.
     *
     * @param memberId 회원 식별자
     * @return 해당 회원이 작성한 모든 리뷰들의 리스트
     */
    List<Review> findByMemberId(Long memberId);
}
