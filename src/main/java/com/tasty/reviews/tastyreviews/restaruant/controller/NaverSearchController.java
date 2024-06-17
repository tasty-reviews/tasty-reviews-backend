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

    // application.properties 파일에서 Naver API의 Client ID와 Client Secret을 주입받음
    @Value("${naver.client.id}")
    private String clientId;

    @Value("${naver.client.secret}")
    private String clientSecret;

    /**
     * 네이버 이미지 검색 API를 호출하여 이미지를 검색하는 메서드입니다.
     *
     * @param query 검색할 이미지의 쿼리 문자열
     * @return ResponseEntity<?> 검색된 이미지 정보를 ResponseEntity로 반환
     */
    @GetMapping("/search/image")
    public ResponseEntity<?> searchNaverImage(@RequestParam String query) {
        // Naver 이미지 검색 API의 URL
        String apiUrl = "https://openapi.naver.com/v1/search/image";
        // 쿼리 파라미터를 URL 인코딩함
        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);

        // URI를 생성함, display 파라미터로 15개의 결과를 요청함
        URI uri = UriComponentsBuilder.fromUriString(apiUrl)
                .queryParam("query", encodedQuery)
                .queryParam("display", 15)
                .build(true)
                .encode(StandardCharsets.UTF_8)
                .toUri();

        // HTTP 요청 헤더를 설정함, Naver API 인증 정보를 추가함
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Naver-Client-Id", clientId);
        headers.set("X-Naver-Client-Secret", clientSecret);
        // HTTP 엔티티를 생성함
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);

        // RestTemplate을 사용하여 API 요청을 수행함
        ResponseEntity<RestaurantImageDTO> responseEntity = new RestTemplate()
                .exchange(uri, HttpMethod.GET, requestEntity, RestaurantImageDTO.class);

        // 응답 본문을 가져옴
        RestaurantImageDTO restaurantImageDTO = responseEntity.getBody();
        // 응답 본문이 null이 아니고, 아이템 리스트가 비어있지 않은 경우
        if (restaurantImageDTO != null && restaurantImageDTO.getItems() != null && !restaurantImageDTO.getItems().isEmpty()) {
            Map<String, Object> images = new HashMap<>();
            // 아이템 리스트에서 최대 4개의 이미지를 추출하여 맵에 저장함
            for (int i = 0; i < Math.min(restaurantImageDTO.getItems().size(), 4); i++) {
                images.put("image" + (i + 1), restaurantImageDTO.getItems().get(i));
            }
            // 응답 상태와 함께 이미지를 반환함
            return ResponseEntity.status(responseEntity.getStatusCode()).body(images);
        }

        // 검색 결과가 없을 경우 NO_CONTENT 상태를 반환함
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
