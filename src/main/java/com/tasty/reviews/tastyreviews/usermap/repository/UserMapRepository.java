package com.tasty.reviews.tastyreviews.usermap.repository;

import com.tasty.reviews.tastyreviews.usermap.domain.UserMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapRepository extends JpaRepository<UserMap, Long> {
    List<UserMap> findByMemberEmail(String email);

    List<UserMap> findByMemberId(Long membberId);

    //내지도 정렬
    @Query("SELECT r FROM UserMap r ORDER BY r.viewCount DESC")
    List<UserMap> getRankingByViewCount();

}
