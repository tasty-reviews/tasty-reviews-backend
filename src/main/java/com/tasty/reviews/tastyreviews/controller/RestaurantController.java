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

//    @PostMapping("/add")
//    public ResponseEntity<RestaurantDTO> registerRestaurant(@RequestBody RestaurantDTO restaurantDTO) {
//        // 현재 로그인한 사용자 정보 가져오기
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        // 인증된 사용자의 이름을 가져옵니다. 이를 사용하여 사용자 정보를 가져올 수 있습니다.
//        String username = authentication.getName();
//
//        // 레스토랑 등록 및 응답 반환
//        RestaurantDTO registeredRestaurant = restaurantService.addRestaurant(restaurantDTO);
//        return ResponseEntity.status(HttpStatus.CREATED).body(registeredRestaurant);
//    }

    @PostMapping("/add")
    public ResponseEntity<String> addRestaurantAndStartReview(@RequestBody RestaurantDTO restaurantDTO) {
        // 현재 로그인한 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 인증된 사용자의 이름을 가져옵니다. 이를 사용하여 사용자 정보를 가져올 수 있습니다.
        String username = authentication.getName();

        try {
            // 음식점 정보 데이터베이스에 저장
            RestaurantDTO savedRestaurant = restaurantService.addRestaurant(restaurantDTO);
            if (savedRestaurant != null) {
                // 리뷰 등록 페이지로 리디렉션할 URL 또는 다른 응답을 반환
                return ResponseEntity.ok("음식점 정보가 성공적으로 저장되었습니다. 리뷰를 등록하세요.");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("음식점 저장 실패");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류 발생: " + e.getMessage());
        }
    }
}
