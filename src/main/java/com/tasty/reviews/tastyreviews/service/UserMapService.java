package com.tasty.reviews.tastyreviews.service;

import com.tasty.reviews.tastyreviews.domain.Member;
import com.tasty.reviews.tastyreviews.domain.Review;
import com.tasty.reviews.tastyreviews.domain.UserMap;
import com.tasty.reviews.tastyreviews.repository.MemberRepository;
import com.tasty.reviews.tastyreviews.repository.UserMapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserMapService {

    private final UserMapRepository userMapRepository;
    private final MemberRepository memberRepository;

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

}


