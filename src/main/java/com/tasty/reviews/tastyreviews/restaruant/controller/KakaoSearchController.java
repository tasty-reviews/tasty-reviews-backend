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

    /**
     * 카카오 API를 이용한 장소 검색을 수행하는 메서드입니다.
     *
     * @param categoryGroupCode 카테고리 그룹 코드 (기본값: "FD6" - 음식점)
     * @param keyword           검색 키워드
     * @return 검색 결과를 문자열 형태로 반환
     */
    @GetMapping("/search")
    public ResponseEntity<String> placeSearch(@RequestParam(defaultValue = "FD6") String categoryGroupCode,
                                              @RequestParam("q") String keyword) {
        return kakaoSearchService.searchPlace(categoryGroupCode, keyword);
    }

    /**
     * 특정 음식점의 상세 정보를 조회하는 메서드입니다.
     *
     * @param placeId 음식점 ID
     * @return 음식점 상세 정보를 담은 DTO를 ResponseEntity로 반환
     */
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
