package com.tasty.reviews.tastyreviews.mail.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 이메일 인증 응답 시 사용할 DTO 클래스입니다.
 */
@Getter
@Setter
public class MailResponseDTO {

    private String authCode; // 인증 코드

}
