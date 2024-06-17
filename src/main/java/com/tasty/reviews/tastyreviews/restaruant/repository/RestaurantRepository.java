package com.tasty.reviews.tastyreviews.restaruant.repository;

import com.tasty.reviews.tastyreviews.restaruant.domain.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    /**
     * 음식점 이름과 도로명 주소로 음식점을 찾는 메서드
     *
     * @param placeName      음식점 이름
     * @param roadAddressName 도로명 주소
     * @return Optional<Restaurant> 객체
     */
    Optional<Restaurant> findByPlaceNameAndRoadAddressName(String placeName, String roadAddressName);

    /**
     * 음식점 ID로 음식점을 찾는 메서드
     *
     * @param placeId 음식점 ID
     * @return Optional<Restaurant> 객체
     */
    Optional<Restaurant> findByPlaceId(String placeId);

    /**
     * 조회수 기준으로 음식점을 내림차순으로 정렬하여 가져오는 메서드
     *
     * @return 조회수 기준 내림차순으로 정렬된 Restaurant 엔티티 리스트
     */
    @Query("SELECT r FROM Restaurant r ORDER BY r.viewCount DESC")
    List<Restaurant> getRankingByViewCount();

    /**
     * 리뷰 개수 기준으로 음식점을 내림차순으로 정렬하여 가져오는 메서드
     *
     * @return 리뷰 개수 기준 내림차순으로 정렬된 Restaurant 엔티티 리스트
     */
    @Query("SELECT r FROM Restaurant r ORDER BY r.reviewCount DESC")
    List<Restaurant> getRankingByReviewCount();
}
