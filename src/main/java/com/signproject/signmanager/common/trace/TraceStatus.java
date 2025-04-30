package com.signproject.signmanager.common.trace;

/**
 * trace.begin()에서 반환되는 로그 상태 객체
 * → end()/exception()에서 로그 종료에 사용됨
 */
public class TraceStatus {
    private final TraceId traceId;
    private final Long startTimeMs;
    private final String message;

    public TraceStatus(TraceId traceId, Long startTimeMs, String message) {
        this.traceId = traceId;
        this.startTimeMs = startTimeMs;
        this.message = message;
    }

    public TraceId getTraceId() {
        return traceId;
    }

    public Long getStartTimeMs() {
        return startTimeMs;
    }

    public String getMessage() {
        return message;
    }
}
