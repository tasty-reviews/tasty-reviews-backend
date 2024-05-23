package com.tasty.reviews.tastyreviews.restaruant.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
@Service
public class KakaoSearchService {

    private static final String KAKAO_API_URL = "https://dapi.kakao.com/v2/local/search/keyword.json";
    private static final String API_KEY = "0f4d1cdf32e4a4f9150145f35c8adc60";

    private final RestaurantService restaurantService;

    public ResponseEntity<String> searchPlace(String categoryGroupCode, String keyword) {
        try {
            URI uri = UriComponentsBuilder.fromUriString(KAKAO_API_URL)
                    .queryParam("category_group_code", categoryGroupCode)
                    .queryParam("query", keyword)
                    .encode(StandardCharsets.UTF_8)
                    .build()
                    .toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "KakaoAK " + API_KEY);

            HttpEntity<?> entity = new HttpEntity<>(headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);

            if (responseEntity.getStatusCode() == HttpStatus.OK) {

                restaurantService.saveRestaurantsFromApiResponse(responseEntity.getBody());
                return ResponseEntity.ok(responseEntity.getBody());
            } else {
                return ResponseEntity.status(responseEntity.getStatusCode()).body("API 호출 실패");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류");
        }
    }
}