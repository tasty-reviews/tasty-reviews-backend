package com.tasty.reviews.tastyreviews.service;

import com.tasty.reviews.tastyreviews.domain.Restaurant;
import com.tasty.reviews.tastyreviews.dto.RestaurantDTO;
import com.tasty.reviews.tastyreviews.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    public RestaurantDTO addRestaurant(RestaurantDTO restaurantDTO) {
        // DTO를 엔티티로 변환
        Restaurant restaurant = restaurantDTO.toEntity();

        // 레스토랑 등록
        Restaurant savedRestaurant = restaurantRepository.save(restaurant);

        // 등록된 레스토랑 정보를 다시 DTO로 변환하여 반환
        return RestaurantDTO.fromEntity(savedRestaurant);
    }

}
