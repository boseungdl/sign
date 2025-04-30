package com.signproject.signmanager.common.exception;

import org.springframework.http.HttpStatus;

/**
 * ✅ [공통 비즈니스 예외 추상 클래스]
 *
 * <p>도메인(비즈니스) 예외의 공통 부모로 사용되며,
 * 각 예외에 고유한 에러 코드와 메시지, 상태 코드를 부여합니다.</p>
 *
 */
public abstract class BusinessException extends RuntimeException {


    private final HttpStatus status;

    /**
     * @param message   예외 메시지
     * @param status    HTTP 상태 코드
     */
    protected BusinessException( String message, HttpStatus status) {
        super(message);
        this.status = status;
    }


    /**
     * HTTP 상태 코드 반환
     */
    public HttpStatus getStatus() {
        return status;
    }
}