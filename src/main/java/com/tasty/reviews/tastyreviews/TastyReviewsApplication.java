package com.tasty.reviews.tastyreviews;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class TastyReviewsApplication {

    public static void main(String[] args) {
        SpringApplication.run(TastyReviewsApplication.class, args);
    }

}