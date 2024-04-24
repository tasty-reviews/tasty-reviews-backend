package com.tasty.reviews.tastyreviews.controller;

import com.tasty.reviews.tastyreviews.dto.RestaurantDTO;
import com.tasty.reviews.tastyreviews.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/restaurant")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    @PostMapping("/add")
    public ResponseEntity<RestaurantDTO> registerRestaurant(@RequestBody RestaurantDTO restaurantDTO) {
        // 현재 로그인한 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 인증된 사용자의 이름을 가져옵니다. 이를 사용하여 사용자 정보를 가져올 수 있습니다.
        String username = authentication.getName();

        // 레스토랑 등록 및 응답 반환
        RestaurantDTO registeredRestaurant = restaurantService.addRestaurant(restaurantDTO, username);
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredRestaurant);
    }
}
