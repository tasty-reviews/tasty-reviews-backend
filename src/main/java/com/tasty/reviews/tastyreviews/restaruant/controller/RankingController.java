package com.tasty.reviews.tastyreviews.restaruant.controller;

import com.tasty.reviews.tastyreviews.restaruant.domain.Restaurant;
import com.tasty.reviews.tastyreviews.restaruant.service.RestaurantService;
import com.tasty.reviews.tastyreviews.usermap.dto.AllUserMapResponseDTO;
import com.tasty.reviews.tastyreviews.usermap.service.UserMapService;
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
    private final UserMapService userMapService;

    @GetMapping
    public ResponseEntity<List<?>> getRankings(@RequestParam("type") String type) {
        switch (type) {
            case "VIEW_COUNT":
                List<Restaurant> viewCountRankings = restaurantService.getRankedRestaurantsByViewCount();
                return ResponseEntity.ok(viewCountRankings);

            case "REVIEW_COUNT":
                List<Restaurant> reviewCountRankings = restaurantService.getRankedRestaurantsByReviewCount();
                return ResponseEntity.ok(reviewCountRankings);

            case "USERMAP_COUNT":
                List<AllUserMapResponseDTO> userMapRankings = userMapService.getRankedUserMapByUserMapCount();
                return ResponseEntity.ok(userMapRankings);

            default:
                return ResponseEntity.badRequest().body(new ArrayList<>());
        }
    }
}
