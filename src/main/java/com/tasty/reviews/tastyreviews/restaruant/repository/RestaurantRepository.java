package com.tasty.reviews.tastyreviews.restaruant.repository;

import com.tasty.reviews.tastyreviews.restaruant.domain.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    Optional<Restaurant> findByPlaceNameAndRoadAddressName(String placeName, String roadAddressName);

    Optional<Restaurant> findByPlaceId(String placeId);

    @Query("SELECT r FROM Restaurant r ORDER BY r.viewCount DESC")
    List<Restaurant> getRankingByViewCount();

    @Query("SELECT r FROM Restaurant r ORDER BY r.reviewCount DESC")
    List<Restaurant> getRankingByReviewCount();

    
}
