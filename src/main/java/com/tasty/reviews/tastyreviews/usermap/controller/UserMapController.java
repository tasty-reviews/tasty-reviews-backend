package com.tasty.reviews.tastyreviews.usermap.controller;

import com.tasty.reviews.tastyreviews.usermap.domain.UserMap;
import com.tasty.reviews.tastyreviews.usermap.dto.AllUserMapResponseDTO;
import com.tasty.reviews.tastyreviews.usermap.dto.UserMapResponseDTO;
import com.tasty.reviews.tastyreviews.usermap.service.UserMapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserMapController {

    private final UserMapService userMapService;

    //모든 회원의 지도 조회
    @GetMapping("/")
    public ResponseEntity<List<AllUserMapResponseDTO>> getAllUserMaps() {
        List<AllUserMapResponseDTO> allUserMaps = userMapService.getAllUserMaps();

        return ResponseEntity.ok(allUserMaps);
    }

    //자신이 작성한 내지도 조회
    @GetMapping("/mymaps")
    public ResponseEntity<List<UserMapResponseDTO>> getUserMaps() {
        try {
            List<UserMapResponseDTO> userMaps = userMapService.getUserMapByEmail();
            return ResponseEntity.ok(userMaps);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(401).build(); // 401 Unauthorized
        }
    }

    //특정사용자가 작성한 내지도 목록 조회
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserMap>> getUserMapsByUserId(@PathVariable Long userId) {
        List<UserMap> userMaps = userMapService.getUserMapsByUserId(userId);
        return ResponseEntity.ok(userMaps);
    }

    //내지도에 저장된 음식점 조회
    //특정 지도 상세정보 (등록된 음식점 목록을 볼 수 있음)
    @GetMapping("/usermaps/{usermapId}")
    public ResponseEntity<UserMap> getRestaurantsByUserMapId(@PathVariable("usermapId") Long usermapId) {
        UserMap userMap = userMapService.getRestaurantsByUserMapId(usermapId);
        return ResponseEntity.ok(userMap);
    }

    //내지도 추가
    @PostMapping("/usermaps/add")
    public ResponseEntity<UserMapResponseDTO> addUserMap(@RequestParam("name") String name,
                                                         @RequestParam("description") String description,
                                                         @RequestParam("userMapImage") MultipartFile file) throws IOException {
        UserMap userMap = new UserMap();
        userMap.setName(name);
        userMap.setDescription(description);

        UserMapResponseDTO createdUserMap = userMapService.createUserMap(userMap, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUserMap);
    }

    //내지도 수정
    @PutMapping("usermaps/{userMapId}")
    public ResponseEntity<UserMap> updateUserMap(@PathVariable Long userMapId,
                                                 @RequestParam("name") String name,
                                                 @RequestParam("description") String description,
                                                 @RequestParam("userMapImage") MultipartFile file) throws IOException {
        UserMap userMapDetails = new UserMap();
        userMapDetails.setName(name);
        userMapDetails.setDescription(description);

        UserMap updatedUserMap = userMapService.updateUserMap(userMapId, userMapDetails, file);
        return ResponseEntity.ok(updatedUserMap);
    }

    //내지도 삭제
    @DeleteMapping("/usermaps/{userMapId}")
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
