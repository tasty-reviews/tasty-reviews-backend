package com.tasty.reviews.tastyreviews.exception;

// 패키지 선언부: 현재 클래스가 속한 패키지를 정의합니다.
// 패키지 이름은 com.tasty.reviews.tastyreviews.exception입니다.

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// 필요한 스프링 프레임워크 클래스들을 임포트합니다.
// HttpStatus: HTTP 상태 코드를 정의하는 열거형 클래스입니다.
// ResponseEntity: HTTP 응답을 나타내는 클래스입니다.
// ExceptionHandler: 특정 예외 타입을 처리하는 메서드에 대한 어노테이션입니다.
// RestControllerAdvice: 컨트롤러 전역에서 발생하는 예외를 처리하는 클래스에 대한 어노테이션입니다.

@RestControllerAdvice
// @RestControllerAdvice 어노테이션은 이 클래스가 모든 @RestController 어노테이션이 달린
// 클래스에서 발생하는 예외를 전역적으로 처리하도록 합니다.
public class ExceptionManager {
    // ExceptionManager 클래스는 예외를 처리하는 메서드를 정의합니다.

    @ExceptionHandler(RuntimeException.class)
    // @ExceptionHandler 어노테이션은 이 메서드가 특정 타입의 예외를 처리하도록 지정합니다.
    // 여기서는 RuntimeException을 처리합니다.
    public ResponseEntity<?> runtimeExceptionHandler(RuntimeException e) {
        // runtimeExceptionHandler 메서드는 RuntimeException이 발생했을 때 호출됩니다.
        // 메서드 인자는 발생한 예외 객체 e를 받습니다.

        return ResponseEntity.status(HttpStatus.CONFLICT)
                // ResponseEntity.status(HttpStatus.CONFLICT) 메서드는 HTTP 상태 코드를 409 CONFLICT로 설정합니다.
                .body(e.getMessage());
        // .body(e.getMessage()) 메서드는 응답 본문에 예외 메시지를 포함시킵니다.
    }
}
