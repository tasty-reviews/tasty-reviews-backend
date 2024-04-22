package com.tasty.reviews.tastyreviews.repository;

import com.tasty.reviews.tastyreviews.domain.RefreshEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface RefreshRepository extends JpaRepository<RefreshEntity, Long> {

    Boolean existsByRefresh(String refresh); //리프레시 토큰 조회

    @Transactional
    void deleteByRefresh(String refresh);
}
