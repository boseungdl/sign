package com.signproject.signmanager.common.exception;

/**
 * ❌ [JWT 토큰 유효성 실패 예외]
 */
public class InvalidTokenException extends AuthException {
    public InvalidTokenException() {
        super("유효하지 않은 토큰입니다.");
    }
}
