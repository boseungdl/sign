package com.signproject.signmanager.controller;

import com.signproject.signmanager.dto.LoginRequestDto;
import com.signproject.signmanager.dto.LoginResponseDto;
import com.signproject.signmanager.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 인증 관련 요청을 처리하는 컨트롤러
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * 로그인 요청 처리
     *
     * @param dto 사용자 입력 (username, password)
     * @return JWT 토큰 문자열 반환
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto requestDto) {
        // ✅ 모든 로직은 서비스에서!
        LoginResponseDto response = authService.login(requestDto);
        return ResponseEntity.ok(response);
    }


}
