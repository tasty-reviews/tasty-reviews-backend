package com.tasty.reviews.tastyreviews.restaruant.controller;

import com.tasty.reviews.tastyreviews.restaruant.dto.RestaurantDTO;
import com.tasty.reviews.tastyreviews.restaruant.service.KakaoSearchService;
import com.tasty.reviews.tastyreviews.restaruant.service.RestaurantService;
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

    //검색
    @GetMapping("/search")
    public ResponseEntity<String> placeSearch(@RequestParam(defaultValue = "FD6") String categoryGroupCode,
                                              @RequestParam("q") String keyword){
        return kakaoSearchService.searchPlace(categoryGroupCode, keyword);
    }

    //음식점 상세보기
    @GetMapping("/place/{place_id}")
    public ResponseEntity<RestaurantDTO> findByPlace(@PathVariable("place_id") String placeId) {
        RestaurantDTO restaurantDto = restaurantService.findByPlace(placeId);

        if (restaurantDto != null) {
            return ResponseEntity.ok(restaurantDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}