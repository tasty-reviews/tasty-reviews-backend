package com.tasty.reviews.tastyreviews.member.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO {

    private String email; // 사용자 이메일
    private String password; // 사용자 비밀번호

}
