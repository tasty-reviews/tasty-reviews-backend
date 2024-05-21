package com.tasty.reviews.tastyreviews.member.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO {

    //이메일, 비밀번호만 받음
    private String email;
    private String password;


}
