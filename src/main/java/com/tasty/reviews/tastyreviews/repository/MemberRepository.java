package com.tasty.reviews.tastyreviews.repository;

import com.tasty.reviews.tastyreviews.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
