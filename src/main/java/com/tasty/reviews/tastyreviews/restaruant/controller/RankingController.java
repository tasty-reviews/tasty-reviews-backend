package com.tasty.reviews.tastyreviews.restaruant.controller;


import com.tasty.reviews.tastyreviews.restaruant.domain.Restaurant;
import com.tasty.reviews.tastyreviews.restaruant.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rankings")
public class RankingController {

    private final RestaurantService restaurantService;

    @GetMapping
    public ResponseEntity<List<Restaurant>> getRankings(@RequestParam("type") String type) {
        List<Restaurant> rankings;
        switch (type) {
            case "VIEW_COUNT" -> rankings = restaurantService.getRankedRestaurantsByViewCount();
            case "REVIEW_COUNT" -> rankings = restaurantService.getRankedRestaurantsByReviewCount();
            default -> rankings = new ArrayList<>(); // 디폴트 케이스에서 초기화
        }

        return ResponseEntity.ok(rankings);
    }
}
