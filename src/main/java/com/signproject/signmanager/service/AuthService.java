// 📁 경로: src/main/java/com/signproject/signmanager/service/AuthService.java

package com.signproject.signmanager.service;

import com.signproject.signmanager.domain.User;
import com.signproject.signmanager.dto.LoginRequestDto;
import com.signproject.signmanager.dto.LoginResponseDto;
import com.signproject.signmanager.dto.UserResponseDto;
import com.signproject.signmanager.repository.UserRepository;
import com.signproject.signmanager.util.JwtTokenProvider;
import com.signproject.signmanager.exception.LoginFailedException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    private final PasswordEncoder passwordEncoder;

    public LoginResponseDto login(LoginRequestDto requestDto) {
        User user = userRepository.findByUsername(requestDto.getUsername())
                .orElseThrow(() -> new LoginFailedException("아이디 또는 비밀번호가 일치하지 않습니다."));

        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new LoginFailedException("아이디 또는 비밀번호가 일치하지 않습니다.");
        }

        String token = jwtTokenProvider.createToken(user.getId());

        // 🔄 분리된 UserResponseDto 사용
        UserResponseDto userDto = new UserResponseDto(user.getId(), user.getUsername(), user.getRole().name());

        return new LoginResponseDto(token, userDto);
    }
}
