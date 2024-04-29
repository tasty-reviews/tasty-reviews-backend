package com.tasty.reviews.tastyreviews.service;

public class Utils {
    // HTML 태그를 제거하는 메소드
    public static String removeHtmlTags(String input) {
        return input.replaceAll("<[^>]*>", "");
    }
}

