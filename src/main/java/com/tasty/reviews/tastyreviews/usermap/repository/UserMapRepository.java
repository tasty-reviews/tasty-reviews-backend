package com.tasty.reviews.tastyreviews.usermap.repository;

import com.tasty.reviews.tastyreviews.usermap.domain.UserMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapRepository extends JpaRepository<UserMap, Long> {

    // 이메일을 기반으로 회원의 내지도 조회
    List<UserMap> findByMemberEmail(String email);

    // 회원 ID를 기반으로 회원의 내지도 조회
    List<UserMap> findByMemberId(Long memberId);

    // 조회수(viewCount) 기준으로 내지도 정렬하여 조회
    @Query("SELECT um FROM UserMap um ORDER BY um.viewCount DESC")
    List<UserMap> getRankingByViewCount();
}
