package com.tasty.reviews.tastyreviews.repository;

import com.tasty.reviews.tastyreviews.domain.Restaurant;
import com.tasty.reviews.tastyreviews.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    Optional<Restaurant> findByPlaceNameAndRoadAddressName(String placeName, String roadAddressName);
}
