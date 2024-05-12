package com.tasty.reviews.tastyreviews.controller;

import com.tasty.reviews.tastyreviews.dto.RestaurantDTO;
import com.tasty.reviews.tastyreviews.service.KakaoSearchService;
import com.tasty.reviews.tastyreviews.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class KakaoSearchController {

    private final RestaurantService restaurantService;
    private final KakaoSearchService kakaoSearchService;

    @GetMapping("/search")
    public ResponseEntity<String> placeSearch(@RequestParam("q") String keyword) {
        return kakaoSearchService.searchPlace(keyword);
    }

    @GetMapping("/place/{id}")
    public ResponseEntity<RestaurantDTO> findByPlace(@PathVariable Long id) {
        RestaurantDTO restaurantDto = restaurantService.findByPlace(id);

        if (restaurantDto != null) {
            return ResponseEntity.ok(restaurantDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}