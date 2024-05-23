package com.tasty.reviews.tastyreviews.member.repository;

import com.tasty.reviews.tastyreviews.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email); //이메일과 일치하는 회원을 찾아줌
    Optional<Member> findByNickname(String nickname); //이메일과 일치하는 회원을 찾아줌

}