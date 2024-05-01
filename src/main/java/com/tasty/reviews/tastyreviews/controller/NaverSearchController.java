package com.tasty.reviews.tastyreviews.controller;

import com.tasty.reviews.tastyreviews.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.Charset;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class NaverSearchController {

    private final RestaurantService restaurantService;

    @GetMapping(value = "/search", produces = "application/json; charset=UTF-8")
    public ResponseEntity<String> searchAndSaveRestaurants(@RequestParam(name = "query") String text) {

        // 네이버 검색 API 클라이언트 ID
        String clientId = "942xEdx8DRSPviV0bqW4";
        String clientSecret = "fQhY5rNaI5";

        try {
            String searchText = text + " 음식점"; // 음식점 카테고리로 검색 범위 제한
            URI uri = UriComponentsBuilder
                    .fromUriString("https://openapi.naver.com")
                    .path("/v1/search/local.json")
                    .queryParam("query", searchText)
                    .queryParam("display", 5)
                    .queryParam("start", 1)
                    .queryParam("sort", "random")
                    .encode(Charset.forName("UTF-8"))
                    .build()
                    .toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.set("X-Naver-Client-Id", clientId);
            headers.set("X-Naver-Client-Secret", clientSecret);
            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, requestEntity, String.class);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                restaurantService.saveRestaurantsFromApiResponse(response.getBody());
                return ResponseEntity.ok("음식점 정보가 성공적으로 저장되었습니다.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("API로부터 유효한 응답을 받지 못했습니다.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("검색 및 저장 과정에서 오류가 발생하였습니다: " + e.getMessage());
        }
    }
}