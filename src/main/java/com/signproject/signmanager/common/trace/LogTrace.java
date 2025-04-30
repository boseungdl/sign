package com.signproject.signmanager.common.trace;

/**
 * 로그 추적 인터페이스 (확장성을 고려해 인터페이스로 분리)
 */
public interface LogTrace {
    TraceStatus begin(String message);
    void end(TraceStatus status);
    void exception(TraceStatus status, Exception e);
}
