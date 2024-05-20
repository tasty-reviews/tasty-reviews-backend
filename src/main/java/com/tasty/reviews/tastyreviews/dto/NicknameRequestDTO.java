package com.tasty.reviews.tastyreviews.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NicknameRequestDTO {

    @NotBlank(message = "닉네임은 필수 항목입니다.")
    @Size(min = 3, max = 20, message = "닉네임은 3자 이상, 20자 이하로 입력해주세요.")
    private String nickName;
}
