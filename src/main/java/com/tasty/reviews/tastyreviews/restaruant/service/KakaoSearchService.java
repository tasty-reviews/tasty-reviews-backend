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

/**
 * 카카오 로컬 API를 이용한 음식점 검색 서비스
 */
@RequiredArgsConstructor
@Service
public class KakaoSearchService {

    // 카카오 API URL과 API 키 상수 선언
    private static final String KAKAO_API_URL = "https://dapi.kakao.com/v2/local/search/keyword.json";
    private static final String API_KEY = "0f4d1cdf32e4a4f9150145f35c8adc60";

    // 의존성 주입: RestaurantService와 ReviewService
    private final RestaurantService restaurantService;
    private final ReviewService reviewService;

    /**
     * 카카오 API를 이용하여 음식점을 검색하는 메서드
     *
     * @param categoryGroupCode 카테고리 그룹 코드
     * @param keyword           검색 키워드
     * @return ResponseEntity<String> 응답 객체
     */
    public ResponseEntity<String> searchPlace(String categoryGroupCode, String keyword) {
        try {
            int size = 15; // 요청할 결과의 수

            List<JSONObject> results = new ArrayList<>(); // 각 페이지의 결과를 저장할 리스트
            JSONArray combinedDocuments = new JSONArray(); // 최종 결과를 저장할 JSON 배열

            // 페이지 반복: 두 페이지에 걸쳐서 결과를 가져옴
            for (int page = 1; page <= 2; page++) {
                // URI 빌드: 카카오 API 요청을 위한 URI 구성
                URI uri = UriComponentsBuilder.fromUriString(KAKAO_API_URL)
                        .queryParam("category_group_code", categoryGroupCode)
                        .queryParam("query", keyword)
                        .queryParam("size", size)
                        .queryParam("page", page) // 페이지 번호 추가
                        .encode(StandardCharsets.UTF_8)
                        .build()
                        .toUri();

                // HTTP 헤더 설정: API 키 추가
                HttpHeaders headers = new HttpHeaders();
                headers.set("Authorization", "KakaoAK " + API_KEY);

                // HTTP 엔티티 생성
                HttpEntity<?> entity = new HttpEntity<>(headers);

                // RestTemplate을 사용하여 API 요청 수행
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);

                // 응답 상태가 200 OK인지 확인
                if (responseEntity.getStatusCode() == HttpStatus.OK) {
                    // 응답 본문을 JSON 객체로 변환
                    JSONObject jsonResponse = new JSONObject(responseEntity.getBody());
                    // documents 배열을 가져옴
                    JSONArray documents = jsonResponse.getJSONArray("documents");
                    // 각 문서를 순회하며 처리
                    for (int i = 0; i < documents.length(); i++) {
                        JSONObject document = documents.getJSONObject(i);

                        // place_name과 road_address_name을 통해 음식점 엔티티를 찾음
                        String placeName = document.getString("place_name");
                        String roadAddressName = document.getString("address_name");

                        Restaurant restaurant = restaurantService.findByPlaceNameAndRoadAddressName(placeName, roadAddressName);

                        // 만약 해당 음식점이 존재하면 평균 평점과 리뷰 개수를 추가
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
                    // API 호출 실패 시 상태 코드와 메시지를 반환
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

            // API 응답으로부터 음식점 정보를 저장
            restaurantService.saveRestaurantsFromApiResponse(combinedResult.toString());

            // 최종 결과 반환
            return ResponseEntity.ok(combinedResult.toString());
        } catch (Exception e) {
            e.printStackTrace();
            // 서버 오류 발생 시 내부 서버 오류 상태 코드와 메시지를 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류");
        }
    }
}
