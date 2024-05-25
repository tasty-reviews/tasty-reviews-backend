package com.tasty.reviews.tastyreviews.controller;

import com.tasty.reviews.tastyreviews.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;

@RestController
@RequiredArgsConstructor
public class KakaoSearchController {

    private static final String KAKAO_API_URL = "https://dapi.kakao.com/v2/local/search/keyword.json";
    private static final String API_KEY = "0f4d1cdf32e4a4f9150145f35c8adc60"; // 개선: API 키를 하드코딩하지 않음

    private final RestaurantService restaurantService;

    /*@GetMapping("/search")
    public ResponseEntity<String> getKakaoApiFromAddress(@RequestParam("query") String keyword) {

        try {
            URI uri = UriComponentsBuilder.fromUriString(KAKAO_API_URL)
                    .queryParam("query", keyword)
                    .encode(StandardCharsets.UTF_8)
                    .build()
                    .toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "KakaoAK " + API_KEY); // 요청 헤더에 API 키 추가

            HttpEntity<?> entity = new HttpEntity<>(headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);

            // 개선: 응답이 성공적으로 수신되었는지 확인
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                return ResponseEntity.ok(responseEntity.getBody());
            } else {
                return ResponseEntity.status(responseEntity.getStatusCode()).body("API 호출 실패");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류");
        }
    }*/

    @GetMapping("/search")
    public ResponseEntity<String> getKakaoApiFromAddress(@RequestParam("q") String keyword) {
        try {
            URI uri = UriComponentsBuilder.fromUriString(KAKAO_API_URL)
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
                // 서비스를 사용하여 응답 데이터 저장
                restaurantService.saveRestaurantsFromApiResponse(responseEntity.getBody());
//                return ResponseEntity.ok("검색 결과가 성공적으로 저장되었습니다.");
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
