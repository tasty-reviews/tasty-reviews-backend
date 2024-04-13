package com.tasty.reviews.tastyreviews.service;

import com.tasty.reviews.tastyreviews.domain.Member;
import com.tasty.reviews.tastyreviews.domain.Restaurant;
import com.tasty.reviews.tastyreviews.domain.Role;
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

    public RestaurantDTO addRestaurant(RestaurantDTO restaurantDTO, String username) {
/*        // 사용자 역할 확인
        if (!isAdmin(username)) {
            throw new IllegalStateException("Only administrators can add restaurants.");
        }*/
        // DTO를 엔티티로 변환
        Restaurant restaurant = restaurantDTO.toEntity();

        // 레스토랑 등록
        Restaurant savedRestaurant = restaurantRepository.save(restaurant);

        // 등록된 레스토랑 정보를 다시 DTO로 변환하여 반환
        return RestaurantDTO.fromEntity(savedRestaurant);
    }

/*    private boolean isAdmin(String username) {
        // 여기에 사용자 이름(username)을 기반으로 사용자 역할을 확인하는 로직을 추가하세요.
        // 예를 들어, 사용자 이름으로 데이터베이스에서 해당 사용자를 조회하여 역할을 확인할 수 있습니다.
        // 이 예제에서는 단순히 "admin"이라는 이름을 가진 사용자만이 관리자로 간주합니다.
        return "admin".equals(username);
    }*/
}
