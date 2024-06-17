package com.tasty.reviews.tastyreviews.global.config;

// 패키지 선언부: 현재 클래스가 속한 패키지를 정의합니다.
// 패키지 이름은 com.tasty.reviews.tastyreviews.global.config입니다.

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// 필요한 스프링 프레임워크 클래스들을 임포트합니다.
// org.springframework.context.annotation.Configuration: 스프링 설정 클래스임을 나타내는 어노테이션입니다.
// org.springframework.web.servlet.config.annotation.CorsRegistry: CORS 설정을 담당하는 클래스입니다.
// org.springframework.web.servlet.config.annotation.WebMvcConfigurer: 스프링 MVC 설정을 구성하기 위한 인터페이스입니다.

@Configuration
// @Configuration 어노테이션은 이 클래스가 스프링 설정 클래스임을 나타냅니다.
public class CorsMvcConfig implements WebMvcConfigurer {
    // CorsMvcConfig 클래스는 WebMvcConfigurer 인터페이스를 구현하여 CORS 설정을 정의합니다.

    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {
        // addCorsMappings 메서드는 CORS 설정을 추가하는 메서드입니다. WebMvcConfigurer 인터페이스에서 정의됩니다.

        corsRegistry.addMapping("/**")
                // corsRegistry.addMapping("/**") 메서드는 모든 경로에 대해 CORS 설정을 추가합니다.
                .allowedOrigins("http://localhost:8090")
                // .allowedOrigins("http://localhost:8090") 메서드는 허용할 오리진을 설정합니다.
                // 여기서는 로컬 개발 서버의 주소인 http://localhost:8090 을 허용합니다.
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                // .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") 메서드는 허용할 HTTP 메서드를 설정합니다.
                .allowedHeaders("*")
                // .allowedHeaders("*") 메서드는 허용할 HTTP 헤더를 설정합니다. 여기서는 모든 헤더를 허용합니다.
                .allowCredentials(true)
                // .allowCredentials(true) 메서드는 인증 정보를 전송할 수 있도록 허용 여부를 설정합니다.
                .maxAge(3600);
        // .maxAge(3600) 메서드는 preflight 요청의 유효 기간을 설정합니다. 여기서는 3600초(1시간)로 설정합니다.
    }
}
