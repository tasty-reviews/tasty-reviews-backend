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

@Service // Spring Service로 선언
@RequiredArgsConstructor // 필수 의존성을 자동으로 주입받는 생성자 생성
@Transactional // 트랜잭션 관리를 위해 추가
public class UserMapService {

    private final UserMapRepository userMapRepository; // UserMap 엔티티의 데이터 액세스를 위한 Repository
    private final MemberRepository memberRepository; // 회원 관련 데이터 액세스를 위한 Repository
    private final RestaurantRepository restaurantRepository; // 음식점 관련 데이터 액세스를 위한 Repository
    private final FileUploadService fileUploadService; // 파일 업로드 서비스 의존성 주입

    // 현재 인증된 회원 반환
    private Member getAuthenticatedMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("User must be logged in");
        }
        String email = authentication.getName();
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("User not found"));
    }

    // 사용자지도 추가 메서드
    public UserMapResponseDTO createUserMap(UserMap userMap, MultipartFile file) throws IOException {
        Member member = getAuthenticatedMember(); // 현재 인증된 회원 정보 가져오기

        // 파일이 업로드되었으면 저장하고 파일명 설정
        if (file != null && !file.isEmpty()) {
            String mymapImage = fileUploadService.storeFile(file);
            userMap.setMyMapImage(mymapImage);
        }

        userMap.setMember(member); // 사용자지도에 회원 설정
        userMapRepository.save(userMap); // 사용자지도 저장

        // 응답 DTO 생성하여 반환
        return UserMapResponseDTO.builder()
                .id(userMap.getId())
                .name(userMap.getName())
                .description(userMap.getDescription())
                .myMapImage(userMap.getMyMapImage())
                .nickname(member.getNickname())
                .viewCount(userMap.getViewCount())
                .build();
    }

    // 사용자지도 수정 메서드
    public UserMap updateUserMap(Long userMapId, UserMap newUserMapDetails, MultipartFile file) throws IOException {
        getAuthenticatedMember(); // 로그인된 사용자 확인

        // 기존 사용자지도 조회
        UserMap userMap = userMapRepository.findById(userMapId)
                .orElseThrow(() -> new RuntimeException("UserMap not found with id " + userMapId));

        // 사용자지도 정보 업데이트
        userMap.setName(newUserMapDetails.getName());
        userMap.setDescription(newUserMapDetails.getDescription());

        // 파일이 업로드되었으면 저장하고 파일명 설정
        if (file != null && !file.isEmpty()) {
            String mymapImage = fileUploadService.storeFile(file);
            userMap.setMyMapImage(mymapImage);
        }

        return userMapRepository.save(userMap); // 업데이트된 사용자지도 저장
    }

    // 사용자지도 삭제 메서드
    public void deleteUserMap(Long userMapId) {
        getAuthenticatedMember(); // 로그인된 사용자 확인

        // 삭제할 사용자지도 조회
        UserMap userMap = userMapRepository.findById(userMapId)
                .orElseThrow(() -> new RuntimeException("UserMap not found with id " + userMapId));
        userMapRepository.delete(userMap); // 사용자지도 삭제
    }

    // 사용자지도에 음식점 추가 메서드
    public UserMap addRestaurantToUserMap(Long userMapId, Long restaurantId) {
        getAuthenticatedMember(); // 로그인된 사용자 확인

        // 사용자지도 조회
        UserMap userMap = userMapRepository.findById(userMapId)
                .orElseThrow(() -> new RuntimeException("UserMap not found"));
        // 음식점 조회
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        userMap.getRestaurants().add(restaurant); // 사용자지도에 음식점 추가
        return userMapRepository.save(userMap); // 변경된 사용자지도 저장
    }

    // 회원이 작성한 사용자지도 리스트 조회
    @Transactional(readOnly = true)
    public List<UserMapResponseDTO> getUserMapByEmail() {
        Member member = getAuthenticatedMember(); // 현재 인증된 회원 정보 가져오기

        List<UserMapResponseDTO> userMapResponseList = new ArrayList<>();

        // 회원의 이메일을 기반으로 사용자지도 조회
        List<UserMap> UserMap = userMapRepository.findByMemberEmail(member.getEmail());

        // 응답 DTO 리스트 생성
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
        return userMapResponseList; // 사용자지도 응답 DTO 리스트 반환
    }

    // 사용자가 작성한 사용자지도 상세 조회
    public UserMap getRestaurantsByUserMapId(Long usermapId) {
        // 사용자지도 조회
        UserMap userMap = userMapRepository.findById(usermapId)
                .orElseThrow(() -> new IllegalArgumentException("UserMap not found with id: " + usermapId));

        userMap.setViewCount(userMap.getViewCount() + 1); // 조회수 증가

        return userMap; // 사용자지도 반환
    }

    // 모든 사용자지도 조회
    public List<AllUserMapResponseDTO> getAllUserMaps() {
        List<UserMap> userMaps = userMapRepository.findAll(); // 모든 사용자지도 조회
        List<AllUserMapResponseDTO> responseDTOList = new ArrayList<>();

        // 응답 DTO 리스트 생성
        for (UserMap userMap : userMaps) {
            AllUserMapResponseDTO responseDTO = AllUserMapResponseDTO.builder()
                    .id(userMap.getId())
                    .name(userMap.getName())
                    .myMapImage(userMap.getMyMapImage())
                    .restaurantCount(userMap.getRestaurants().size())
                    .nickname(userMap.getMember().getNickname())
                    .viewCount(userMap.getViewCount())
                    .build();

            responseDTOList.add(responseDTO);
        }

        return responseDTOList; // 모든 사용자지도 응답 DTO 리스트 반환
    }

    // 특정 사용자가 작성한 사용자지도 리스트 조회
    public List<UserMap> getUserMapsByUserId(Long userId) {
        return userMapRepository.findByMemberId(userId); // 사용자 ID를 기반으로 사용자지도 조회
    }

    // 조회수(viewCount) 기준으로 사용자지도 랭킹 조회
    @Transactional(readOnly = true)
    public List<AllUserMapResponseDTO> getRankedUserMapByUserMapCount() {
        // 조회수 기준으로 정렬된 사용자지도 조회
        List<UserMap> rankingByViewCount = userMapRepository.getRankingByViewCount();
        List<AllUserMapResponseDTO> responseDTOList = new ArrayList<>();

        // 응답 DTO 리스트 생성
        for (UserMap userMap : rankingByViewCount) {
            AllUserMapResponseDTO responseDTO = AllUserMapResponseDTO.builder()
                    .id(userMap.getId())
                    .name(userMap.getName())
                    .nickname(userMap.getMember().getNickname())
                    .myMapImage(userMap.getMyMapImage())
                    .description(userMap.getDescription())
                    .restaurantCount(userMap.getRestaurants().size())
                    .viewCount(userMap.getViewCount())
                    .build();

            responseDTOList.add(responseDTO);
        }
        return responseDTOList; // 사용자지도 랭킹 응답 DTO 리스트 반환
    }
}
