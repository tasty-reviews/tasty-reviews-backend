package com.tasty.reviews.tastyreviews.service;

import com.tasty.reviews.tastyreviews.domain.Member;
import com.tasty.reviews.tastyreviews.domain.Restaurant;
import com.tasty.reviews.tastyreviews.domain.UserMap;
import com.tasty.reviews.tastyreviews.repository.MemberRepository;
import com.tasty.reviews.tastyreviews.repository.RestaurantRepository;
import com.tasty.reviews.tastyreviews.repository.UserMapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserMapService {

    private final UserMapRepository userMapRepository;
    private final MemberRepository memberRepository;
    private final RestaurantRepository restaurantRepository;

    //사용자지도 추가
    public UserMap createUserMap(UserMap userMap) {

        // 사용자의 인증 정보를 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 사용자가 로그인되어 있는지 확인
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("User must be logged in to create a userMap");
        }

        String email = authentication.getName();

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("user not found"));

        userMap.setMember(member);

        return userMapRepository.save(userMap);
    }

    //사용자지도 수정
    public UserMap updateUserMap(Long userMapId, UserMap newUserMapDetails) {

        // 사용자의 인증 정보를 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 사용자가 로그인되어 있는지 확인
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("User must be logged in to update a userMap");
        }

        UserMap userMap = userMapRepository.findById(userMapId)
                .orElseThrow(() -> new RuntimeException("UserMap not found with id " + userMapId));

        userMap.setName(newUserMapDetails.getName());
        userMap.setDescription(newUserMapDetails.getDescription());
        userMap.setImageurl(newUserMapDetails.getImageurl());

        return userMapRepository.save(userMap);

    }

    //사용자지도 삭제
    public void deleteUserMap(Long userMapId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("User must be logged in to delete a user map");
        }

        UserMap userMap = userMapRepository.findById(userMapId)
                .orElseThrow(() -> new RuntimeException("UserMap not found with id " + userMapId));
        userMapRepository.delete(userMap);
    }

    //사용자지도에 음식점 추가
    public UserMap addRestaurantToUserMap(Long userMapId, Long restaurantId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("User must be logged in to delete a user map");
        }

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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("User must be logged in to delete a user map");
        }

        String email = authentication.getName();
        return userMapRepository.findByMemberEmail(email);
    }
}


