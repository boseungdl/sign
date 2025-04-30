package com.signproject.signmanager.config;

import com.signproject.signmanager.common.trace.LogTrace;
import com.signproject.signmanager.common.trace.TraceStatus;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * ✅ [공통 로깅 및 처리 시간 측정 인터셉터] (수정)
 * - 모든 API 요청에 대해 LogTrace를 사용한 트레이싱 시작/종료 추가
 */
@Slf4j
@RequiredArgsConstructor // 추가: LogTrace 주입을 위한 생성자
@Component
public class LoggingInterceptor implements HandlerInterceptor {

    private static final String START_TIME_ATTR = "startTime";
    private static final String TRACE_STATUS_ATTR = "traceStatus"; // 추가: TraceStatus 저장 키

    private final LogTrace trace; // 추가: 트레이스 로거

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {
        request.setAttribute(START_TIME_ATTR, System.currentTimeMillis());
        TraceStatus status = trace.begin(
                String.format("[HTTP] %s %s", request.getMethod(), request.getRequestURI())
        );
        request.setAttribute(TRACE_STATUS_ATTR, status);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) {
        TraceStatus status = (TraceStatus) request.getAttribute(TRACE_STATUS_ATTR);
        if (status != null) {
            if (ex != null) trace.exception(status, ex);
            else trace.end(status);
        }
    }
}
