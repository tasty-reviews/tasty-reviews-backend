package com.tasty.reviews.tastyreviews.config;

import com.tasty.reviews.tastyreviews.jwt.JWTFilter;
import com.tasty.reviews.tastyreviews.jwt.JWTUtil;
import com.tasty.reviews.tastyreviews.jwt.LoginFilter;
import lombok.RequiredArgsConstructor;
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
import org.springframework.web.cors.CorsConfiguration;

import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    //AuthenticationManager가 인자로 받을 AuthenticationConfiguraion 객체 생성자 주입
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;

    //AuthenticationManager Bean 등록
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

        return configuration.getAuthenticationManager();
    }

    //패스워드 암호 메서드
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .cors((corsCustomizer) -> corsCustomizer.configurationSource(request -> {

                    CorsConfiguration configuration = new CorsConfiguration();

                    configuration.setAllowedOrigins(Collections.singletonList("http://localhost:8081"));
                    configuration.setAllowedMethods(Collections.singletonList("*"));
                    configuration.setAllowCredentials(true);
                    configuration.setAllowedHeaders(Collections.singletonList("*"));
                    configuration.setMaxAge(3600L);

                    configuration.setExposedHeaders(Collections.singletonList("Authorization"));

                    return configuration;
                }));

        http
                .formLogin(AbstractHttpConfigurer::disable) //form 로그인 방식 disable
                .httpBasic(AbstractHttpConfigurer::disable) //http 인증 방식 disable
                .csrf(AbstractHttpConfigurer::disable) // csrk disable

                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/", "/main", "/join")
                        .permitAll()
                        .requestMatchers("/admin")
                        .hasRole("ADMIN")
                        .anyRequest().authenticated()
                );
        http
                .addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class);

        //csrf 토큰 disable
        http
                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil), UsernamePasswordAuthenticationFilter.class);


        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

}
