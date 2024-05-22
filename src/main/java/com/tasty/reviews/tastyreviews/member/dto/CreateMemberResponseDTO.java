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

    private String email;
    private String nickname;

    @Builder
    public CreateMemberResponseDTO(Member member) {
        this.email = member.getEmail();
        this.nickname = member.getNickname();
    }
}
