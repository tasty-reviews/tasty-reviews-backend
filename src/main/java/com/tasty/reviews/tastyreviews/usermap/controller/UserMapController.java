package com.tasty.reviews.tastyreviews.usermap.controller;

import com.tasty.reviews.tastyreviews.usermap.domain.UserMap;
import com.tasty.reviews.tastyreviews.usermap.service.UserMapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserMapController {

    private final UserMapService userMapService;

    //내지도 조회
    @GetMapping("/usermaps")
    public ResponseEntity<List<UserMap>> getUserMaps() {
        try {
            List<UserMap> userMaps = userMapService.getUserMapByEmail();
            return ResponseEntity.ok(userMaps);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(401).build(); // 401 Unauthorized
        }
    }

    //내지도 추가
    @PostMapping("/usermap/add")
    public ResponseEntity<UserMap> addUserMap(@RequestBody UserMap userMap) {
        UserMap createduserMap = userMapService.createUserMap(userMap);
        return ResponseEntity.status(HttpStatus.CREATED).body(createduserMap);
    }

    //내지도 수정
     @PutMapping("usermap/{userMapId}")
    public ResponseEntity<UserMap> updateUserMap(@PathVariable Long userMapId, @RequestBody UserMap userMapDetails) {
        UserMap updatedUserMap = userMapService.updateUserMap(userMapId, userMapDetails);
        return ResponseEntity.ok(updatedUserMap);
    }

    //내지도 삭제
    @DeleteMapping("/usermap/{userMapId}")
    public ResponseEntity<Void> deleteuserMap(@PathVariable Long userMapId) {
        userMapService.deleteUserMap(userMapId);
        return ResponseEntity.noContent().build();
    }

    //음식점 추가
    @PostMapping("/userMaps/{userMapId}/addRestaurant/{restaurantId}")
    public ResponseEntity<UserMap> addRestaurantToUserMap(@PathVariable Long userMapId, @PathVariable Long restaurantId) {
        UserMap updatedUserMap = userMapService.addRestaurantToUserMap(userMapId, restaurantId);
        return ResponseEntity.ok(updatedUserMap);
    }
}