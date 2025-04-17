package com.signproject.signmanager.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * ✅ [공통 로깅 및 처리 시간 측정 인터셉터]
 *
 * 📌 역할 및 목적:
 * - 모든 API 요청에 대해 로깅 및 성능 분석을 수행
 * - 요청 메서드/URI, 응답 상태 코드, 처리 시간(ms) 등을 기록하여 운영·디버깅·모니터링에 활용
 * - 예외 여부와 관계없이 afterCompletion에서 최종 로깅 수행
 *
 * ⚠️ REST API 환경에서는 postHandle보다 afterCompletion 사용을 권장
 */
@Slf4j
@Component
public class LoggingInterceptor implements HandlerInterceptor {

    private static final String START_TIME_ATTR = "startTime";

    /**
     * 컨트롤러 진입 전: 요청 URI, HTTP 메서드, 시작 시간 기록
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        long startTime = System.currentTimeMillis();
        request.setAttribute(START_TIME_ATTR, startTime);

        log.info("📥 [Request] {} {}", request.getMethod(), request.getRequestURI());
        return true;
    }

    /**
     * 컨트롤러 처리 완료 후 (예외 포함): 응답 상태, 처리 시간(ms) 로깅
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        long startTime = (Long) request.getAttribute(START_TIME_ATTR);
        long duration = System.currentTimeMillis() - startTime;

        int status = response.getStatus();
        String method = request.getMethod();
        String uri = request.getRequestURI();

        if (ex != null) {
            log.warn("❌ [Exception] {} {} → status={} ({}ms) → ex: {}", method, uri, status, duration, ex.getClass().getSimpleName());
        } else {
            log.info("📤 [Response] {} {} → status={} ({}ms)", method, uri, status, duration);
        }
    }
}
