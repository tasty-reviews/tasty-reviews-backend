package com.tasty.reviews.tastyreviews.controller;

import com.tasty.reviews.tastyreviews.domain.UserMap;
import com.tasty.reviews.tastyreviews.service.UserMapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserMapController {

    private final UserMapService userMapService;

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

}
