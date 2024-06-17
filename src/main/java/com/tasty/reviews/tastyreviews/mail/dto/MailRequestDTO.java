package com.tasty.reviews.tastyreviews.mail.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 이메일 전송 요청 시 사용할 DTO 클래스입니다.
 */
@Getter
@Setter
public class MailRequestDTO {

    private String authCode; // 전송할 인증 코드

}
