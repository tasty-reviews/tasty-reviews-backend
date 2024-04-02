package com.tasty.reviews.tastyreviews.dto;

import com.tasty.reviews.tastyreviews.domain.Gender;
import com.tasty.reviews.tastyreviews.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateMemberDTO { //회원가입에서 클라이언트가 보낸 정보를 전달하는 DTO

    private String email;
    private String password;
    private String nickname;
    private Gender gender;
    private int age;

    /*DTO -> Entity*/
    public Member toEntity() {
        return Member.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .gender(gender)
                .age(age)
                .build();
    }
}
