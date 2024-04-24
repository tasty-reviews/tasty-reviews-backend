package com.tasty.reviews.tastyreviews.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {

    @GetMapping("/admin")
    private String adminP() {
        return "admin Controller";
    }
}
