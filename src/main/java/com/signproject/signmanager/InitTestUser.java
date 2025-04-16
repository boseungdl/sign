package com.signproject.signmanager;

import com.signproject.signmanager.domain.User;
import com.signproject.signmanager.domain.enums.Role;
import com.signproject.signmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 애플리케이션 실행 시 테스트용 유저 등록
 */
@Configuration
@RequiredArgsConstructor
public class InitTestUser {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initUser() {
        return args -> {
            if (userRepository.findByUsername("testuser").isEmpty()) {
                String encoded = passwordEncoder.encode("1234");
                userRepository.save(new User("testuser", encoded, Role.USER));
                System.out.println("✅ 테스트 유저 등록 완료 (username: testuser / password: 1234)");
            }
        };
    }
}
