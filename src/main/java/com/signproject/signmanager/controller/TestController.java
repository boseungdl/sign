package com.signproject.signmanager.controller;

import com.signproject.signmanager.common.annotation.Login;
import com.signproject.signmanager.common.response.ApiResponse;
import com.signproject.signmanager.dto.UserInfoDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 공통 로깅 인터셉터 테스트용 컨트롤러
 * - /api/test 로 요청을 보내면 요청/응답 로그가 찍히는지 확인
 */
@RestController
public class TestController {

    /**
     * 공통 로깅 테스트용
     */
    @GetMapping("/api/test")
    public ResponseEntity<ApiResponse<String>> testLogging() {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK, "로깅 인터셉터 정상 작동 ✅", null));
    }

    /**
     * 로그인 유저 정보 확인
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserInfoDto>> getMyInfo(@Login UserInfoDto userInfo) {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK, "내 정보 조회 성공", userInfo));
    }
}
