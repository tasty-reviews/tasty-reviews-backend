package com.tasty.reviews.tastyreviews.usermap.service;

import com.tasty.reviews.tastyreviews.member.domain.Member;
import com.tasty.reviews.tastyreviews.member.repository.MemberRepository;
import com.tasty.reviews.tastyreviews.restaruant.domain.Restaurant;
import com.tasty.reviews.tastyreviews.restaruant.repository.RestaurantRepository;
import com.tasty.reviews.tastyreviews.upload.service.FileUploadService;
import com.tasty.reviews.tastyreviews.usermap.domain.UserMap;
import com.tasty.reviews.tastyreviews.usermap.dto.AllUserMapResponseDTO;
import com.tasty.reviews.tastyreviews.usermap.dto.UserMapResponseDTO;
import com.tasty.reviews.tastyreviews.usermap.repository.UserMapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
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
    public UserMapResponseDTO createUserMap(UserMap userMap, MultipartFile file) throws IOException {

        Member member = getAuthenticatedMember();

        if (file != null && !file.isEmpty()) {
            String mymapImage = fileUploadService.storeFile(file);
            userMap.setMyMapImage(mymapImage);
        }

        userMap.setMember(member);
        userMapRepository.save(userMap);

        return UserMapResponseDTO.builder()
                .id(userMap.getId())
                .name(userMap.getName())
                .description(userMap.getDescription())
                .myMapImage(userMap.getMyMapImage())
                .nickname(member.getNickname())
                .viewCount(userMap.getViewCount())
                .build();

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
    public List<UserMapResponseDTO> getUserMapByEmail() {
        Member member = getAuthenticatedMember();

        List<UserMapResponseDTO> userMapResponseList = new ArrayList<>();

        List<UserMap> UserMap = userMapRepository.findByMemberEmail(member.getEmail());

        for (UserMap userMaps : UserMap) {
            UserMapResponseDTO userMapResponseDTO = UserMapResponseDTO.builder()
                    .id(userMaps.getId())
                    .name(userMaps.getName())
                    .description(userMaps.getDescription())
                    .myMapImage(userMaps.getMyMapImage())
                    .nickname(userMaps.getMember().getNickname())
                    .viewCount(userMaps.getViewCount() + 1)
                    .build();

            userMapResponseList.add(userMapResponseDTO);
        }
        return userMapResponseList;
    }

    //리스트에 저장된 음식점 조회
    public UserMap getRestaurantsByUserMapId(Long usermapId) {
        UserMap userMap = userMapRepository.findById(usermapId)
                .orElseThrow(() -> new IllegalArgumentException("UserMap not found with id: " + usermapId));

        userMap.setViewCount(userMap.getViewCount() + 1);

        return userMap;
    }

    //모든 내 지도 조회
    public List<AllUserMapResponseDTO> getAllUserMaps() {
        List<UserMap> userMaps = userMapRepository.findAll();
        List<AllUserMapResponseDTO> responseDTOList = new ArrayList<>();

        for (UserMap userMap : userMaps) {
            AllUserMapResponseDTO responseDTO = AllUserMapResponseDTO.builder()
                    .id(userMap.getId())
                    .name(userMap.getName())
                    .myMapImage(userMap.getMyMapImage())
                    .resturanstCount(userMap.getRestaurants().size())
                    .nickname(userMap.getMember().getNickname())
                    .viewCount(userMap.getViewCount())
                    .build();

            responseDTOList.add(responseDTO);
        }

        return responseDTOList;
    }

    //특정 사용자가 작성한 사용지지도 리스트 조회
    public List<UserMap> getUserMapsByUserId(Long userId) {
        return userMapRepository.findByMemberId(userId);
    }

    //내지도 랭킹
    @Transactional(readOnly = true)
    public List<AllUserMapResponseDTO> getRankedUserMapByUserMapCount() {

        List<UserMap> rankingByViewCount = userMapRepository.getRankingByViewCount();

        List<AllUserMapResponseDTO> responseDTOList = new ArrayList<>();

        for (UserMap userMap : rankingByViewCount) {

            AllUserMapResponseDTO responseDTO = AllUserMapResponseDTO.builder()
                    .id(userMap.getId())
                    .name(userMap.getName())
                    .nickname(userMap.getMember().getNickname())
                    .myMapImage(userMap.getMyMapImage())
                    .description(userMap.getDescription())
                    .resturanstCount(userMap.getRestaurants().size())
                    .viewCount(userMap.getViewCount())
                    .build();

            responseDTOList.add(responseDTO);

        }
        return responseDTOList;
    }
}


