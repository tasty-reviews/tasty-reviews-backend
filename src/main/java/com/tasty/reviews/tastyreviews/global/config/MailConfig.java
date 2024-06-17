package com.tasty.reviews.tastyreviews.global.config;

// 패키지 선언부: 현재 클래스가 속한 패키지를 정의합니다.
// 패키지 이름은 com.tasty.reviews.tastyreviews.global.config입니다.

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

// 필요한 스프링 프레임워크 클래스들을 임포트합니다.
// org.springframework.beans.factory.annotation.Value: 애플리케이션 속성을 주입받기 위한 어노테이션입니다.
// org.springframework.context.annotation.Bean: 빈을 정의하기 위한 어노테이션입니다.
// org.springframework.context.annotation.Configuration: 스프링 설정 클래스임을 나타내는 어노테이션입니다.
// org.springframework.mail.javamail.JavaMailSender: 메일을 보낼 수 있는 인터페이스입니다.
// org.springframework.mail.javamail.JavaMailSenderImpl: JavaMailSender 인터페이스의 구현체입니다.
// java.util.Properties: 키와 값의 쌍으로 이루어진 속성 파일을 다루기 위한 클래스입니다.

@Configuration
// @Configuration 어노테이션은 이 클래스가 스프링 설정 클래스임을 나타냅니다.
public class MailConfig {
    // MailConfig 클래스는 메일 전송 설정을 정의합니다.

    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.port}")
    private int port;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;

    @Value("${spring.mail.properties.mail.smtp.auth}")
    private boolean auth;

    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    private boolean starttlsEnable;

    @Value("${spring.mail.properties.mail.smtp.starttls.required}")
    private boolean starttlsRequired;

    @Value("${spring.mail.properties.mail.smtp.connectiontimeout}")
    private int connectionTimeout;

    @Value("${spring.mail.properties.mail.smtp.timeout}")
    private int timeout;

    @Value("${spring.mail.properties.mail.smtp.writetimeout}")
    private int writeTimeout;

    // @Value 어노테이션은 애플리케이션 속성 파일(application.properties 또는 application.yml)에서
    // 해당 속성 값을 주입받습니다.

    @Bean
    // @Bean 어노테이션은 이 메서드가 스프링 컨텍스트에서 관리하는 빈을 생성함을 나타냅니다.
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        // JavaMailSenderImpl 객체를 생성합니다.

        mailSender.setHost(host);
        // 메일 서버의 호스트를 설정합니다.
        mailSender.setPort(port);
        // 메일 서버의 포트를 설정합니다.
        mailSender.setUsername(username);
        // 메일 서버의 사용자 이름을 설정합니다.
        mailSender.setPassword(password);
        // 메일 서버의 비밀번호를 설정합니다.
        mailSender.setDefaultEncoding("UTF-8");
        // 메일의 기본 인코딩을 UTF-8로 설정합니다.
        mailSender.setJavaMailProperties(getMailProperties());
        // 메일 서버 속성(Properties)을 설정합니다.

        return mailSender;
        // 설정이 완료된 JavaMailSender 객체를 반환합니다.
    }

    private Properties getMailProperties() {
        // getMailProperties 메서드는 메일 서버 속성을 설정합니다.

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", auth);
        // SMTP 인증 여부를 설정합니다.
        properties.put("mail.smtp.starttls.enable", starttlsEnable);
        // STARTTLS 사용 여부를 설정합니다.
        properties.put("mail.smtp.starttls.required", starttlsRequired);
        // STARTTLS 요구 여부를 설정합니다.
        properties.put("mail.smtp.connectiontimeout", connectionTimeout);
        // SMTP 연결 타임아웃을 설정합니다.
        properties.put("mail.smtp.timeout", timeout);
        // SMTP 타임아웃을 설정합니다.
        properties.put("mail.smtp.writetimeout", writeTimeout);
        // SMTP 쓰기 타임아웃을 설정합니다.

        return properties;
        // 설정된 속성(Properties) 객체를 반환합니다.
    }
}
