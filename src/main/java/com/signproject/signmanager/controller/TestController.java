package com.signproject.signmanager.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 공통 로깅 인터셉터 테스트용 컨트롤러
 * - /api/test 로 요청을 보내면 요청/응답 로그가 찍히는지 확인
 */
@RestController
public class TestController {

    @GetMapping("/api/test")
    public String testLogging() {
        return "로깅 인터셉터 정상 작동 ✅";
    }
}
