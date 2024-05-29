package com.tasty.reviews.tastyreviews.service;


import com.tasty.reviews.tastyreviews.review.domain.Review;
import com.tasty.reviews.tastyreviews.review.service.ReviewService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class ReviewServiceTest {

    @Autowired
    ReviewService reviewService;


    @Test
    public void 음식점리뷰조회() {
        List<Review> reviews = new ArrayList<>();

//        reviews.add(new Review(1L, null, null, 1, "good", "img1"));
//        reviews.add(new Review(2L, null, null, 1, "great", "img2"));



    }


}