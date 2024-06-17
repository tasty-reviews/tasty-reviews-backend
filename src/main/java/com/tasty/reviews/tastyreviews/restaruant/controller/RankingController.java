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

    /**
     * 주어진 타입(type)에 따라 다양한 랭킹 정보를 조회하는 메서드입니다.
     *
     * @param type 랭킹 타입 (VIEW_COUNT, REVIEW_COUNT, USERMAP_COUNT 중 하나)
     * @return ResponseEntity<List<?>> 랭킹 정보를 담은 ResponseEntity
     */
    @GetMapping
    public ResponseEntity<List<?>> getRankings(@RequestParam("type") String type) {
        switch (type) {
            case "VIEW_COUNT":
                // 조회수 기준 음식점 랭킹 리스트를 조회함
                List<Restaurant> viewCountRankings = restaurantService.getRankedRestaurantsByViewCount();
                return ResponseEntity.ok(viewCountRankings);

            case "REVIEW_COUNT":
                // 리뷰 수 기준 음식점 랭킹 리스트를 조회함
                List<Restaurant> reviewCountRankings = restaurantService.getRankedRestaurantsByReviewCount();
                return ResponseEntity.ok(reviewCountRankings);

            case "USERMAP_COUNT":
                // 사용자지도 수 기준 사용자지도 랭킹 리스트를 조회함
                List<AllUserMapResponseDTO> userMapRankings = userMapService.getRankedUserMapByUserMapCount();
                return ResponseEntity.ok(userMapRankings);

            default:
                // 잘못된 타입이 요청된 경우 빈 리스트와 함께 BadRequest 반환
                return ResponseEntity.badRequest().body(new ArrayList<>());
        }
    }
}
