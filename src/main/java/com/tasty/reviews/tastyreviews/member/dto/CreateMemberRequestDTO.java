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
public class CreateMemberRequestDTO {

    @Email(message = "이메일 형식이 아닙니다")
    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    private String email; // 이메일

    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
    private String password; // 비밀번호

    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9-_]{2,10}$", message = "닉네임은 특수문자를 제외한 2~10자리여야 합니다.")
    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    private String nickname; // 닉네임

    @NotNull(message = "나이는 필수 입력값 입니다.")
    private Integer age; // 나이

    private String gender; // 성별

    private Role role; // 권한

    /* DTO -> Entity 변환 메서드 */
    public Member toEntity() {
        return Member.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .age(age)
                .gender(gender)
                .role(Role.USER) // 회원 가입 시 기본적으로 USER 권한을 할당
                .build();
    }
}
