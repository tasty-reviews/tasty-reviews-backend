package com.tasty.reviews.tastyreviews.member.repository;

import com.tasty.reviews.tastyreviews.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 회원(Member) 엔티티를 관리하는 JpaRepository입니다.
 * JpaRepository를 상속받아 기본적인 CRUD(Create, Read, Update, Delete) 기능을 제공합니다.
 */
@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    /**
     * 이메일과 일치하는 회원을 찾습니다.
     *
     * @param email 찾고자 하는 회원의 이메일
     * @return Optional<Member> 이메일과 일치하는 회원 엔티티 (존재할 경우), 없으면 빈 Optional 객체
     */
    Optional<Member> findByEmail(String email);

    /**
     * 닉네임과 일치하는 회원을 찾습니다.
     *
     * @param nickname 찾고자 하는 회원의 닉네임
     * @return Optional<Member> 닉네임과 일치하는 회원 엔티티 (존재할 경우), 없으면 빈 Optional 객체
     */
    Optional<Member> findByNickname(String nickname);
}
