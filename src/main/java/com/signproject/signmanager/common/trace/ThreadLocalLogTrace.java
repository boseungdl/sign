package com.signproject.signmanager.common.trace;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * ThreadLocal을 이용해 traceId를 스레드별로 유지
 * 깊이에 따라 로그 계층 구조를 표현
 */
@Component
public class ThreadLocalLogTrace implements LogTrace {

    private static final Logger log = LoggerFactory.getLogger(ThreadLocalLogTrace.class);
    private final ThreadLocal<TraceId> traceIdHolder = new ThreadLocal<>();

    @Override
    public TraceStatus begin(String message) {
        syncTraceId();
        TraceId traceId = traceIdHolder.get();
        long startTime = System.currentTimeMillis();
        log.info("[{}] {}{}", traceId.getId(), addSpace("-->", traceId.getLevel()), message);
        return new TraceStatus(traceId, startTime, message);
    }

    @Override
    public void end(TraceStatus status) {
        complete(status, null);
    }

    @Override
    public void exception(TraceStatus status, Exception e) {
        complete(status, e);
    }

    private void complete(TraceStatus status, Exception e) {
        long stopTime = System.currentTimeMillis();
        long resultTime = stopTime - status.getStartTimeMs();
        TraceId traceId = status.getTraceId();

        if (e == null) {
            log.info("[{}] {}{} time={}ms", traceId.getId(), addSpace("<--", traceId.getLevel()), status.getMessage(), resultTime);
        } else {
            log.info("[{}] {}{} time={}ms ex={}", traceId.getId(), addSpace("<X-", traceId.getLevel()), status.getMessage(), resultTime, e.toString());
        }

        releaseTraceId();
    }

    private void syncTraceId() {
        TraceId traceId = traceIdHolder.get();
        traceIdHolder.set(traceId == null ? new TraceId() : traceId.createNextId());
    }

    private void releaseTraceId() {
        TraceId traceId = traceIdHolder.get();
        if (traceId.isFirstLevel()) {
            traceIdHolder.remove();
        } else {
            traceIdHolder.set(traceId.createPreviousId());
        }
    }

    private String addSpace(String prefix, int level) {
        return "|   ".repeat(level) + prefix;
    }
}
