package com.tasty.reviews.tastyreviews.member.dto;

import com.tasty.reviews.tastyreviews.member.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateMemberResponseDTO {

    private String email; // 회원 이메일
    private String nickname; // 회원 닉네임

    @Builder
    public CreateMemberResponseDTO(Member member) {
        this.email = member.getEmail(); // Member 엔티티의 이메일을 설정
        this.nickname = member.getNickname(); // Member 엔티티의 닉네임을 설정
    }
}
