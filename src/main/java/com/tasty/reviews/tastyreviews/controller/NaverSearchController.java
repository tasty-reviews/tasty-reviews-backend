package com.tasty.reviews.tastyreviews.controller;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.Charset;

@RestController
public class NaverSearchController {

    @GetMapping(value = "/search", produces = "application/json; charset=UTF-8")
    public ResponseEntity<String> naverSearchList(@RequestParam(name = "query") String text) {

        // 네이버 검색 API 클라이언트 ID
        String clientId = "942xEdx8DRSPviV0bqW4";
        String clientSecret = "fQhY5rNaI5";

        URI uri = UriComponentsBuilder
                .fromUriString("https://openapi.naver.com")
                .path("/v1/search/local.json")
                .queryParam("query", text)
                .queryParam("display", 5)
                .queryParam("start", 1)
                .queryParam("sort", "random")
                .encode(Charset.forName("UTF-8"))
                .build()
                .toUri();

        //요청헤더에 API id, 시크릿키 추가
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Naver-Client-Id", clientId);
        headers.set("X-Naver-Client-Secret", clientSecret);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, requestEntity, String.class);

        String responseBody = responseEntity.getBody();

        return ResponseEntity.ok(responseBody);
    }
}