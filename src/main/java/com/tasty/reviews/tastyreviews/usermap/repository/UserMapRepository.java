package com.tasty.reviews.tastyreviews.usermap.repository;

import com.tasty.reviews.tastyreviews.restaruant.domain.Restaurant;
import com.tasty.reviews.tastyreviews.usermap.domain.UserMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserMapRepository extends JpaRepository<UserMap, Long> {
    List<UserMap> findByMemberEmail(String email);

    Optional<Restaurant> findByName(String name);

    List<UserMap> findByMemberId(Long membberId);

}
