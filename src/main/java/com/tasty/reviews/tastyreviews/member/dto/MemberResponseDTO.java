package com.tasty.reviews.tastyreviews.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class MemberResponseDTO implements Serializable {

    private String email;
    private String nickname;

    @Builder
    public MemberResponseDTO(String email, String nickname) {
        this.email = email;
        this.nickname = nickname;
    }
}
