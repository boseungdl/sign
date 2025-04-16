package com.signproject.signmanager.config;

import lombok.RequiredArgsConstructor;
import com.signproject.signmanager.config.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security 설정 클래스
 * - 기본 로그인/Basic 인증 비활성화
 * - JWT 인증 필터 등록
 */
@Configuration
@RequiredArgsConstructor // ✅ 필드 기반 생성자 자동 생성 (jwtAuthFilter 초기화)
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter; // ✅ 생성자 주입 대상

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .formLogin(form -> form.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class); // ✅ 필터 인스턴스 주입

        return http.build();
    }
}
