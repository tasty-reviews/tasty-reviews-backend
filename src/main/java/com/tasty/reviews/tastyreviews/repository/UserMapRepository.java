package com.tasty.reviews.tastyreviews.repository;

import com.tasty.reviews.tastyreviews.domain.UserMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapRepository extends JpaRepository<UserMap, Long> {
    List<UserMap> findByMemberEmail(String email);

}
