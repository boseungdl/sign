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
 * - username: tmdqh0 (5~20자 / 영문+숫자)
 * - password: Shkshk1212! (8~20자 / 영문+숫자+특수문자 포함)
 */
@Configuration
@RequiredArgsConstructor
public class InitTestUser {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initUser() {
        return args -> {
            String username = "tmdqh0";
            String rawPassword = "Shkshk1212!";

            if (userRepository.findByUsername(username).isEmpty()) {
                String encoded = passwordEncoder.encode(rawPassword);
                userRepository.save(new User(username, encoded, Role.USER));

                System.out.printf("✅ 테스트 유저 등록 완료 (username: %s / password: %s)%n", username, rawPassword);
            }
        };
    }
}
