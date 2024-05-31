package com.tasty.reviews.tastyreviews.usermap.service;

import com.tasty.reviews.tastyreviews.member.domain.Member;
import com.tasty.reviews.tastyreviews.restaruant.domain.Restaurant;
import com.tasty.reviews.tastyreviews.upload.service.FileUploadService;
import com.tasty.reviews.tastyreviews.usermap.domain.UserMap;
import com.tasty.reviews.tastyreviews.member.repository.MemberRepository;
import com.tasty.reviews.tastyreviews.restaruant.repository.RestaurantRepository;
import com.tasty.reviews.tastyreviews.usermap.repository.UserMapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserMapService {

    private final UserMapRepository userMapRepository;
    private final MemberRepository memberRepository;
    private final RestaurantRepository restaurantRepository;
    private final FileUploadService fileUploadService;

    private Member getAuthenticatedMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("User must be logged in");
        }
        String email = authentication.getName();
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("User not found"));
    }

    //사용자지도 추가
    public UserMap createUserMap(UserMap userMap, MultipartFile file) throws IOException {

        Member member = getAuthenticatedMember();

        if (file != null && !file.isEmpty()) {
            String mymapImage = fileUploadService.storeFile(file);
            userMap.setMyMapImage(mymapImage);
        }

        userMap.setMember(member);
        return userMapRepository.save(userMap);
    }

    //사용자지도 수정
    public UserMap updateUserMap(Long userMapId, UserMap newUserMapDetails, MultipartFile file) throws IOException {
        getAuthenticatedMember(); // 로그인된 사용자인지 확인

        UserMap userMap = userMapRepository.findById(userMapId)
                .orElseThrow(() -> new RuntimeException("UserMap not found with id " + userMapId));

        userMap.setName(newUserMapDetails.getName());
        userMap.setDescription(newUserMapDetails.getDescription());
        if (file != null && !file.isEmpty()) {
            String mymapImage = fileUploadService.storeFile(file);
            userMap.setMyMapImage(mymapImage);
        }

        return userMapRepository.save(userMap);

    }

    //사용자지도 삭제
    public void deleteUserMap(Long userMapId) {
        getAuthenticatedMember(); // 로그인된 사용자인지 확인

        UserMap userMap = userMapRepository.findById(userMapId)
                .orElseThrow(() -> new RuntimeException("UserMap not found with id " + userMapId));
        userMapRepository.delete(userMap);
    }

    //사용자지도에 음식점 추가
    public UserMap addRestaurantToUserMap(Long userMapId, Long restaurantId) {
        getAuthenticatedMember(); // 로그인된 사용자인지 확인

        UserMap userMap = userMapRepository.findById(userMapId)
                .orElseThrow(() -> new RuntimeException("UserMap not found"));
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        userMap.getRestaurants().add(restaurant); // UserMap과 Restaurant 관계 설정
        return userMapRepository.save(userMap); // 변경 사항 저장
    }

    //사용자가 작성한 사용자지도 리스트 조회
    @Transactional(readOnly = true)
    public List<UserMap> getUserMapByEmail() {
        Member member = getAuthenticatedMember();

        return userMapRepository.findByMemberEmail(member.getEmail());
    }

    //리스트에 저장된 음식점 조회
    public UserMap getRestaurantsByUserMapId(Long usermapId) {
        UserMap userMap = userMapRepository.findById(usermapId)
                .orElseThrow(() -> new IllegalArgumentException("UserMap not found with id: " + usermapId));

        return userMap;
    }

    //특정 사용자가 작성한 사용지지도 리스트 조회
    public List<UserMap> getUserMapsByUserId(Long userId) {
        return userMapRepository.findByMemberId(userId);
    }
}


