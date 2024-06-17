package com.tasty.reviews.tastyreviews.usermap.controller;

import com.tasty.reviews.tastyreviews.usermap.domain.UserMap;
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

    private final UserMapService userMapService; // UserMapService 의존성 주입

    /**
     * 자신이 작성한 내지도 조회
     * @return 자신이 작성한 내지도 목록을 ResponseEntity로 반환
     */
    @GetMapping("/mymaps")
    public ResponseEntity<List<UserMapResponseDTO>> getUserMaps() {
        try {
            List<UserMapResponseDTO> userMaps = userMapService.getUserMapByEmail();
            return ResponseEntity.ok(userMaps); // 조회된 내지도 목록 반환
        } catch (IllegalStateException e) {
            return ResponseEntity.status(401).build(); // 인증 실패 시 401 Unauthorized 반환
        }
    }

    /**
     * 특정 사용자가 작성한 내지도 목록 조회
     * @param userId 사용자 ID
     * @return 특정 사용자가 작성한 내지도 목록을 ResponseEntity로 반환
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserMap>> getUserMapsByUserId(@PathVariable Long userId) {
        List<UserMap> userMaps = userMapService.getUserMapsByUserId(userId);
        return ResponseEntity.ok(userMaps); // 조회된 사용자의 내지도 목록 반환
    }

    /**
     * 내지도에 저장된 음식점 조회
     * @param usermapId 내지도 ID
     * @return 특정 내지도에 등록된 음식점 정보를 ResponseEntity로 반환
     */
    @GetMapping("/usermaps/{usermapId}")
    public ResponseEntity<UserMap> getRestaurantsByUserMapId(@PathVariable("usermapId") Long usermapId) {
        UserMap userMap = userMapService.getRestaurantsByUserMapId(usermapId);
        return ResponseEntity.ok(userMap); // 특정 내지도에 등록된 음식점 정보 반환
    }

    /**
     * 내지도 추가
     * @param name 내지도 이름
     * @param description 내지도 설명
     * @param file 내지도 이미지 파일
     * @return 생성된 내지도 정보를 ResponseEntity로 반환
     * @throws IOException 파일 처리 중 발생 가능한 예외
     */
    @PostMapping("/usermaps/add")
    public ResponseEntity<UserMapResponseDTO> addUserMap(@RequestParam("name") String name,
                                                         @RequestParam("description") String description,
                                                         @RequestParam("userMapImage") MultipartFile file) throws IOException {
        UserMap userMap = new UserMap(); // 새로운 내지도 객체 생성
        userMap.setName(name);
        userMap.setDescription(description);

        UserMapResponseDTO createdUserMap = userMapService.createUserMap(userMap, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUserMap); // 생성된 내지도 정보 반환
    }

    /**
     * 내지도 수정
     * @param userMapId 내지도 ID
     * @param name 내지도 이름
     * @param description 내지도 설명
     * @param file 내지도 이미지 파일
     * @return 수정된 내지도 정보를 ResponseEntity로 반환
     * @throws IOException 파일 처리 중 발생 가능한 예외
     */
    @PutMapping("usermaps/{userMapId}")
    public ResponseEntity<UserMap> updateUserMap(@PathVariable Long userMapId,
                                                 @RequestParam("name") String name,
                                                 @RequestParam("description") String description,
                                                 @RequestParam("userMapImage") MultipartFile file) throws IOException {
        UserMap userMapDetails = new UserMap(); // 수정할 내지도 객체 생성
        userMapDetails.setName(name);
        userMapDetails.setDescription(description);

        UserMap updatedUserMap = userMapService.updateUserMap(userMapId, userMapDetails, file);
        return ResponseEntity.ok(updatedUserMap); // 수정된 내지도 정보 반환
    }

    /**
     * 내지도 삭제
     * @param userMapId 삭제할 내지도 ID
     * @return 삭제 완료를 나타내는 ResponseEntity 반환
     */
    @DeleteMapping("/usermaps/{userMapId}")
    public ResponseEntity<Void> deleteuserMap(@PathVariable Long userMapId) {
        userMapService.deleteUserMap(userMapId); // 내지도 삭제 메서드 호출
        return ResponseEntity.noContent().build(); // 삭제 완료 응답
    }

    /**
     * 음식점 추가
     * @param userMapId 내지도 ID
     * @param restaurantId 음식점 ID
     * @return 음식점 추가 후의 내지도 정보를 ResponseEntity로 반환
     */
    @PostMapping("/userMaps/{userMapId}/addRestaurant/{restaurantId}")
    public ResponseEntity<UserMap> addRestaurantToUserMap(@PathVariable Long userMapId, @PathVariable Long restaurantId) {
        UserMap updatedUserMap = userMapService.addRestaurantToUserMap(userMapId, restaurantId);
        return ResponseEntity.ok(updatedUserMap); // 음식점을 추가한 후의 내지도 정보 반환
    }
}
