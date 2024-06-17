package com.tasty.reviews.tastyreviews.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateRequestPasswordDTO {

    @NotBlank(message = "현재 비밀번호는 필수 입력 값입니다.")
    private String currentPassword; // 현재 비밀번호를 저장하는 필드

    @NotBlank(message = "새 비밀번호는 필수 입력 값입니다.")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}",
            message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.") // 비밀번호 형식을 검증하는 정규식 패턴
    private String newPassword; // 변경할 새 비밀번호를 저장하는 필드

    // Lombok을 사용하여 자동 생성된 기본 생성자
    // 이 클래스는 비밀번호 변경 요청 시 클라이언트가 전송하는 DTO로 사용됩니다.
    // 현재 비밀번호와 새 비밀번호는 반드시 비어있지 않아야 하며, 새 비밀번호는 지정된 형식을 만족해야 합니다.
}
