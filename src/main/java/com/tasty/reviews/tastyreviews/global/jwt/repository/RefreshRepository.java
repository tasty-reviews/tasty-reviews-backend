package com.tasty.reviews.tastyreviews.global.jwt.repository;

import com.tasty.reviews.tastyreviews.global.jwt.damain.RefreshEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface RefreshRepository extends JpaRepository<RefreshEntity, Long> {

    Boolean existsByRefresh(String refresh); // 리프레시 토큰이 존재하는지 여부를 확인하는 메서드

    @Transactional
    void deleteByRefresh(String refresh); // 주어진 리프레시 토큰 값으로 데이터베이스에서 해당 엔티티를 삭제하는 메서드

    @Transactional
    void deleteByUserEmail(String email); // 주어진 사용자 이메일로 데이터베이스에서 해당 엔티티를 삭제하는 메서드
}
