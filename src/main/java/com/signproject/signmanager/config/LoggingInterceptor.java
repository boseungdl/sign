package com.signproject.signmanager.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 모든 API 요청/응답을 로깅하는 공통 인터셉터
 * - 요청 URL, HTTP 메서드
 * - 응답 상태 코드
 * 를 기록함으로써 디버깅 및 추적에 사용
 */
@Slf4j
@Component
public class LoggingInterceptor implements HandlerInterceptor {

    /**
     * 컨트롤러 진입 전 요청 정보를 기록
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        log.info("📥 [Request] {} {}", request.getMethod(), request.getRequestURI());
        return true;
    }

    /**
     * 컨트롤러 처리 후 응답 정보를 기록
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        log.info("📤 [Response] {} {} → status={}", request.getMethod(), request.getRequestURI(), response.getStatus());
    }
}
