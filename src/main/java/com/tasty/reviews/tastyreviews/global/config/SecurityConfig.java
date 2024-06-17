package com.tasty.reviews.tastyreviews.global.config;

// 패키지 선언부: 현재 클래스가 속한 패키지를 정의합니다.
// 패키지 이름은 com.tasty.reviews.tastyreviews.global.config입니다.

import com.tasty.reviews.tastyreviews.global.jwt.config.CustomLogoutFilter;
import com.tasty.reviews.tastyreviews.global.jwt.config.JWTFilter;
import com.tasty.reviews.tastyreviews.global.jwt.service.JWTUtil;
import com.tasty.reviews.tastyreviews.global.jwt.config.LoginFilter;
import com.tasty.reviews.tastyreviews.member.repository.MemberRepository;
import com.tasty.reviews.tastyreviews.global.jwt.repository.RefreshRepository;
import jakarta.servlet.SessionCookieConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Collections;

// 필요한 스프링 프레임워크 클래스들과 프로젝트 내 다른 클래스를 임포트합니다.
// CORS 설정, JWT 필터, 로그인 필터, 로그아웃 필터 등을 사용합니다.

@Configuration
// @Configuration 어노테이션은 이 클래스가 스프링 설정 클래스임을 나타냅니다.
@EnableWebSecurity
// @EnableWebSecurity 어노테이션은 웹 보안 활성화 설정을 나타냅니다.
@RequiredArgsConstructor
// @RequiredArgsConstructor 어노테이션은 final 필드에 대한 생성자를 자동으로 생성합니다.
public class SecurityConfig {

    // final 필드 선언: 생성자를 통해 주입될 필드들입니다.
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;
    private final MemberRepository memberRepository;

    // AuthenticationManager Bean 등록
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        // authenticationManager 메서드는 AuthenticationManager를 생성하여 반환합니다.
        return configuration.getAuthenticationManager();
    }

    // 패스워드 암호화 메서드
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        // bCryptPasswordEncoder 메서드는 BCryptPasswordEncoder 객체를 생성하여 반환합니다.
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // filterChain 메서드는 보안 필터 체인을 설정합니다.

        http
                .cors((corsCustomizer) -> corsCustomizer.configurationSource(request -> {
                    // CORS 설정을 추가합니다.
                    CorsConfiguration configuration = new CorsConfiguration();
                    configuration.setAllowedOrigins(Collections.singletonList("http://localhost:8090"));
                    // 허용할 오리진을 설정합니다.
                    configuration.setAllowedMethods(Collections.singletonList("*"));
                    // 허용할 HTTP 메서드를 설정합니다.
                    configuration.setAllowCredentials(true);
                    // 인증 정보를 허용합니다.
                    configuration.setAllowedHeaders(Collections.singletonList("*"));
                    // 허용할 헤더를 설정합니다.
                    configuration.setMaxAge(3600L);
                    // preflight 요청의 유효 기간을 설정합니다.
                    configuration.setExposedHeaders(Collections.singletonList("Authorization"));
                    // 노출할 헤더를 설정합니다.
                    return configuration;
                }));

        http
                .formLogin(AbstractHttpConfigurer::disable) // form 로그인 방식 비활성화
                .httpBasic(AbstractHttpConfigurer::disable) // http 기본 인증 비활성화
                .csrf(AbstractHttpConfigurer::disable) // CSRF 비활성화

                .authorizeHttpRequests((auth) -> auth
                                .requestMatchers("/", "/main", "/join", "/verify", "/auth-code")
                                .permitAll()
                                // 특정 경로에 대한 접근을 허용합니다.
                                .requestMatchers("/search", "/search/image")
                                .permitAll()
                                .requestMatchers("/place/**")
                                .permitAll()
                                .requestMatchers("/api/restaurants/**")
                                .permitAll()
                                .requestMatchers("/rankings/**","/usermaps/{usermapId}", "/user/{userId}")
                                .permitAll()
                                .requestMatchers("/reissue")
                                .permitAll()
                                .requestMatchers("/mypage/**").hasRole("USER")
                                // 특정 경로에 대한 접근을 USER 역할로 제한합니다.
                                .requestMatchers("/api/reviews/image/**")
                                .permitAll() // 이미지에 대한 요청은 모든 사용자에게 허용
                                .anyRequest().authenticated()
                        // 그 외 모든 요청은 인증된 사용자만 접근할 수 있습니다.
                );

        http
                .addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class)
                // JWT 필터를 로그인 필터 앞에 추가합니다.
                .addFilterBefore(new CustomLogoutFilter(jwtUtil, refreshRepository), LogoutFilter.class);
        // 커스텀 로그아웃 필터를 로그아웃 필터 앞에 추가합니다.

        http
                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil, refreshRepository, memberRepository), UsernamePasswordAuthenticationFilter.class);
        // 로그인 필터를 UsernamePasswordAuthenticationFilter 위치에 추가합니다.

        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        // 세션 관리를 STATELESS 모드로 설정합니다.

        return http.build();
        // 설정된 HttpSecurity 객체를 빌드하여 반환합니다.
    }

    @Bean
    public ServletContextInitializer servletContextInitializer() {
        // servletContextInitializer 메서드는 서블릿 컨텍스트 초기화를 설정합니다.
        return servletContext -> {
            SessionCookieConfig sessionCookieConfig = servletContext.getSessionCookieConfig();
            sessionCookieConfig.setName("JSESSIONID");
            // 세션 쿠키의 이름을 설정합니다.
            sessionCookieConfig.setPath("/");
            // 세션 쿠키의 경로를 설정합니다.
            sessionCookieConfig.setMaxAge(3600);
            // 세션 쿠키의 유효 기간을 설정합니다.
            sessionCookieConfig.setSecure(false);
            // 세션 쿠키의 Secure 속성을 설정합니다.
            sessionCookieConfig.setHttpOnly(false);
            // 세션 쿠키의 HttpOnly 속성을 설정합니다.
            sessionCookieConfig.setComment("SameSite=None; Secure"); // 추가
            // 세션 쿠키의 SameSite 속성과 Secure 속성을 설정합니다.
        };
    }
}
