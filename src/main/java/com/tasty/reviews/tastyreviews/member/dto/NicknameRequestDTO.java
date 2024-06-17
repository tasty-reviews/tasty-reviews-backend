package com.tasty.reviews.tastyreviews.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NicknameRequestDTO {

    @NotBlank(message = "닉네임은 필수 항목입니다.") // 닉네임이 비어 있으면 안 됨을 나타내는 검증 애노테이션
    @Size(min = 3, max = 20, message = "닉네임은 3자 이상, 20자 이하로 입력해주세요.") // 닉네임의 길이 제한을 나타내는 검증 애노테이션
    private String nickName; // 변경할 닉네임을 저장하는 필드

    // Lombok을 사용하여 자동 생성된 기본 생성자
    // 이 클래스는 닉네임 변경 요청 시 클라이언트가 전송하는 DTO로 사용됩니다.
    // 닉네임은 반드시 비어있지 않고, 3자 이상 20자 이하로 제한됩니다.
}
