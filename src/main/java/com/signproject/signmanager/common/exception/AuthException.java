package com.signproject.signmanager.common.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ✅ [공통 인증 예외 추상 클래스]
 *
 * 📌 목적:
 * - 인증 실패 상황 (ex. 토큰 없음, 만료, 위조 등)에 대한 예외를 통합 관리
 */
public abstract class AuthException extends RuntimeException { // <-- 반드시 RuntimeException 상속

    private static final Logger log = LoggerFactory.getLogger(AuthException.class);

    public AuthException(String message) {
        super(message);
        log.warn("[AuthException 발생] {}", message);
    }
}
