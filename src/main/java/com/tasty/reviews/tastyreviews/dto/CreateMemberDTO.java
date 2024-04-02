package com.tasty.reviews.tastyreviews.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class CreateMemberDTO { //회원가입에서 클라이언트가 보낸 정보를 전달하는 DTO

    private String email;
    private String password;
    private String nickname;
    private String gender;
    private int age;
}
