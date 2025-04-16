package com.signproject.signmanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security 설정 클래스
 * - 기본 로그인/Basic 인증 비활성화
 * - 로그인 API는 인증 없이 접근 허용
 */
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())             // CORS 허용
                .csrf(csrf -> csrf.disable())                // CSRF 비활성화 (API 서버)
                .formLogin(form -> form.disable())           // 기본 로그인 폼 제거
                .httpBasic(httpBasic -> httpBasic.disable()) // HTTP Basic 인증 제거
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll() // 로그인 등 인증 없이 접근 허용
                        .anyRequest().authenticated()                // 그 외는 인증 필요
                );

        return http.build();
    }
}
