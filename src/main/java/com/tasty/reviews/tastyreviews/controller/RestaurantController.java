package com.tasty.reviews.tastyreviews.controller;

import com.tasty.reviews.tastyreviews.dto.RestaurantDTO;
import com.tasty.reviews.tastyreviews.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/restaurant")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;


    @PostMapping("/add")
    public ResponseEntity<RestaurantDTO> registerRestaurant(@RequestBody RestaurantDTO restaurantDTO) {
        RestaurantDTO registeredRestaurant = restaurantService.addRestaurant(restaurantDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredRestaurant);
    }
}
