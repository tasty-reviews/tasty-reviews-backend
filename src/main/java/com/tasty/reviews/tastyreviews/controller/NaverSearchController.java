package com.tasty.reviews.tastyreviews.controller;

import com.tasty.reviews.tastyreviews.dto.RestaurantImageDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@RestController
public class NaverSearchController {

    @Value("${naver.client.id}")
    private String clientId;

    @Value("${naver.client.secret}")
    private String clientSecret;

    @GetMapping("/search/image")
    public ResponseEntity<?> searchNaverImage(@RequestParam String query) {
        // 네이버 이미지 검색 API의 엔드포인트 URL
        String apiUrl = "https://openapi.naver.com/v1/search/image";

        // 검색어를 URL로 인코딩
        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);

        // API 호출을 위한 URI 생성
        URI uri = UriComponentsBuilder.fromUriString(apiUrl)
                .queryParam("query", encodedQuery)
                .queryParam("display", 15)
                .build(true)
                .encode(StandardCharsets.UTF_8)
                .toUri();

        // API 호출에 필요한 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Naver-Client-Id", clientId);
        headers.set("X-Naver-Client-Secret", clientSecret);
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);

        // REST 호출하여 API 응답을 받아옴
        ResponseEntity<RestaurantImageDTO> responseEntity = new RestTemplate()
                .exchange(uri, HttpMethod.GET, requestEntity, RestaurantImageDTO.class);

        // API 응답에서 첫 번째 이미지 정보만 추출
        RestaurantImageDTO restaurantImageDTO = responseEntity.getBody();
        RestaurantImageDTO.SearchImageItem firstImage = null;
        if (restaurantImageDTO != null && restaurantImageDTO.getItems() != null && !restaurantImageDTO.getItems().isEmpty()) {
            firstImage = restaurantImageDTO.getItems().get(0);
        }

        // 클라이언트에게 첫 번째 이미지 정보만 반환
        return ResponseEntity.status(responseEntity.getStatusCode()).body(firstImage);
    }
}
