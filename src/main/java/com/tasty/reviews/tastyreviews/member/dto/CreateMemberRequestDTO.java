
package com.tasty.reviews.tastyreviews.member.dto;

import com.tasty.reviews.tastyreviews.member.domain.Member;
import com.tasty.reviews.tastyreviews.member.domain.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateMemberRequestDTO { //회원가입에서 클라이언트가 보낸 정보를 전달하는 DTO

    @Email
    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    private String email;

    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
    private String password;

    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9-_]{2,10}$", message = "닉네임은 특수문자를 제외한 2~10자리여야 합니다.")
    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    private String nickname;

    @NotNull(message = "나이는 필수 입력값 입니다.")
    private Integer age;

    private String gender;

    private Role role;

    /*DTO -> Entity*/
    public Member toEntity() {
        return Member.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .age(age)
                .gender(gender)
                .role(Role.USER)
                .build();
    }

}