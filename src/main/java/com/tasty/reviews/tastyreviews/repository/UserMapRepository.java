package com.tasty.reviews.tastyreviews.repository;

import com.tasty.reviews.tastyreviews.domain.UserMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapRepository extends JpaRepository<UserMap, Long> {
}
