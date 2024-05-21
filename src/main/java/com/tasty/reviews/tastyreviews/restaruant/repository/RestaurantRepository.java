package com.tasty.reviews.tastyreviews.restaruant.repository;

import com.tasty.reviews.tastyreviews.restaruant.domain.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    Optional<Restaurant> findByPlaceNameAndRoadAddressName(String placeName, String roadAddressName);
}
