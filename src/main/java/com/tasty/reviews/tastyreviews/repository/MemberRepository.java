package com.tasty.reviews.tastyreviews.repository;

import com.tasty.reviews.tastyreviews.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email); //이메일과 일치하는 회원을 찾아줌
    Optional<Member> findByNickname(String nickname); //이메일과 일치하는 회원을 찾아줌

}
