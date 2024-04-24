package com.tasty.reviews.tastyreviews.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @GetMapping("/main")
    private String main() {
        return "main Controller";
    }
}
