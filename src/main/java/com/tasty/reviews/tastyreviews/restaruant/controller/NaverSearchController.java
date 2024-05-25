package com.tasty.reviews.tastyreviews.restaruant.controller;

import com.tasty.reviews.tastyreviews.restaruant.dto.RestaurantImageDTO;
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
        String apiUrl = "https://openapi.naver.com/v1/search/image";
        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);

        URI uri = UriComponentsBuilder.fromUriString(apiUrl)
                .queryParam("query", encodedQuery)
                .queryParam("display", 15)
                .build(true)
                .encode(StandardCharsets.UTF_8)
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Naver-Client-Id", clientId);
        headers.set("X-Naver-Client-Secret", clientSecret);
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<RestaurantImageDTO> responseEntity = new RestTemplate()
                .exchange(uri, HttpMethod.GET, requestEntity, RestaurantImageDTO.class);

        RestaurantImageDTO restaurantImageDTO = responseEntity.getBody();
        if (restaurantImageDTO != null && restaurantImageDTO.getItems() != null && !restaurantImageDTO.getItems().isEmpty()) {
            Map<String, Object> images = new HashMap<>();
            for (int i = 0; i < Math.min(restaurantImageDTO.getItems().size(), 4); i++) {
                images.put("image" + (i + 1), restaurantImageDTO.getItems().get(i));
            }
            return ResponseEntity.status(responseEntity.getStatusCode()).body(images);
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
