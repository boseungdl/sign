package com.signproject.signmanager.controller;

import com.signproject.signmanager.common.response.ApiResponse;
import com.signproject.signmanager.dto.LoginRequestDto;
import com.signproject.signmanager.dto.LoginResponseDto;
import com.signproject.signmanager.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
     * @param requestDto 사용자 입력 (username, password)
     * @return JWT 토큰 및 사용자 정보 포함된 ApiResponse
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDto>> login(@Valid @RequestBody LoginRequestDto requestDto) {
        LoginResponseDto response = authService.login(requestDto);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK, "로그인 성공", response));
    }
}
