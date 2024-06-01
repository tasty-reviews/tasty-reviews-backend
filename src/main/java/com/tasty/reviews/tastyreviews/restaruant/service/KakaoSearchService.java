package com.tasty.reviews.tastyreviews.restaruant.service;

import com.tasty.reviews.tastyreviews.restaruant.domain.Restaurant;
import com.tasty.reviews.tastyreviews.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class KakaoSearchService {

    private static final String KAKAO_API_URL = "https://dapi.kakao.com/v2/local/search/keyword.json";
    private static final String API_KEY = "0f4d1cdf32e4a4f9150145f35c8adc60";

    private final RestaurantService restaurantService;
    private final ReviewService reviewService; // ReviewService 추가

    public ResponseEntity<String> searchPlace(String categoryGroupCode, String keyword) {
        try {
            int size = 15;

            List<JSONObject> results = new ArrayList<>();
            JSONArray combinedDocuments = new JSONArray();

            for (int page = 1; page <= 2; page++) {
                URI uri = UriComponentsBuilder.fromUriString(KAKAO_API_URL)
                        .queryParam("category_group_code", categoryGroupCode)
                        .queryParam("query", keyword)
                        .queryParam("size", size)
                        .queryParam("page", page) // 페이지 번호 추가
                        .encode(StandardCharsets.UTF_8)
                        .build()
                        .toUri();

                HttpHeaders headers = new HttpHeaders();
                headers.set("Authorization", "KakaoAK " + API_KEY);

                HttpEntity<?> entity = new HttpEntity<>(headers);

                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);

                if (responseEntity.getStatusCode() == HttpStatus.OK) {
                    JSONObject jsonResponse = new JSONObject(responseEntity.getBody());
                    JSONArray documents = jsonResponse.getJSONArray("documents");
                    for (int i = 0; i < documents.length(); i++) {
                        JSONObject document = documents.getJSONObject(i);

                        // place_name과 road_address_name을 통해 음식점 엔티티를 찾음
                        String placeName = document.getString("place_name");
                        String roadAddressName = document.getString("address_name");

                        Restaurant restaurant = restaurantService.findByPlaceNameAndRoadAddressName(placeName, roadAddressName);

                        if (restaurant != null) {
                            Double avgRating = reviewService.getAvgRatingByRestaurantId(restaurant.getId());
                            int reviewCount = reviewService.getReviewCountByRestaurantId(restaurant.getId());

                            document.put("avgRating", avgRating);
                            document.put("reviewCount", reviewCount);
                        }

                        combinedDocuments.put(document);
                    }
                    results.add(jsonResponse);
                } else {
                    return ResponseEntity.status(responseEntity.getStatusCode()).body("API 호출 실패");
                }
            }

            // 전체 결과를 JSON 객체로 합침
            JSONObject combinedResult = new JSONObject();
            if (!results.isEmpty()) {
                JSONObject meta = results.get(0).getJSONObject("meta");
                combinedResult.put("meta", meta);
            }
            combinedResult.put("documents", combinedDocuments);

            restaurantService.saveRestaurantsFromApiResponse(combinedResult.toString());

            return ResponseEntity.ok(combinedResult.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류");
        }
    }
}
