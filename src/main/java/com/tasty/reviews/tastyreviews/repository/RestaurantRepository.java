package com.tasty.reviews.tastyreviews.repository;

import com.tasty.reviews.tastyreviews.domain.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
}
