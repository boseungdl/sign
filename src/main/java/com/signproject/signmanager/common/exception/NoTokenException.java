package com.signproject.signmanager.common.exception;


/**
 * ❌ [JWT 토큰 누락 예외]
 */
public class NoTokenException extends AuthException {
    public NoTokenException() {
        super("인증 토큰이 존재하지 않습니다.");
    }
}
