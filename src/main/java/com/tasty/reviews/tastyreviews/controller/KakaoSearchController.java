package com.tasty.reviews.tastyreviews.controller;

import com.tasty.reviews.tastyreviews.service.KakaoSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class KakaoSearchController {

    private final KakaoSearchService kakaoSearchService;

    @GetMapping("/search")
    public ResponseEntity<String> placeSearch(@RequestParam("q") String keyword) {
        return kakaoSearchService.searchPlace(keyword);
    }
}