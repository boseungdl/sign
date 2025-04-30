package com.signproject.signmanager.common.aop;

import com.signproject.signmanager.common.trace.LogTrace;
import com.signproject.signmanager.common.trace.TraceStatus;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * AOP 기반 로그 추적기
 * - Controller, Service, Repository 계층 자동 추적
 */
@Aspect
@Component
@RequiredArgsConstructor
public class LogTraceAspect {

    private final LogTrace trace;

    @Around("execution(* com.signproject.signmanager..controller..*(..)) || execution(* com.signproject.signmanager..service..*(..)) || execution(* com.signproject.signmanager..repository..*(..))")
    public Object doTrace(ProceedingJoinPoint joinPoint) throws Throwable {
        String message = joinPoint.getSignature().toShortString();
        TraceStatus status = trace.begin(message);
        try {
            Object result = joinPoint.proceed();
            trace.end(status);
            return result;
        } catch (Exception e) {
            trace.exception(status, e);
            throw e;
        }
    }
}
