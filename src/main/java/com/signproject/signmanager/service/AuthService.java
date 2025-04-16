package com.signproject.signmanager.service;

import com.signproject.signmanager.domain.User;
import com.signproject.signmanager.dto.LoginRequestDto;
import com.signproject.signmanager.repository.UserRepository;
import com.signproject.signmanager.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.NoSuchElementException;

/**
 * 로그인 로직을 처리하는 서비스 클래스
 * - 사용자 검증
 * - 비밀번호 일치 여부 확인
 * - JWT 토큰 생성
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder; // 추가

    public String login(LoginRequestDto dto) {
        User user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 사용자입니다."));

        // ✅ 암호화된 비밀번호 비교
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return jwtTokenProvider.createToken(user.getId());
    }
}
