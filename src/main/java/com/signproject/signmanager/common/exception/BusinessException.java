package com.signproject.signmanager.common.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ✅ [공통 비즈니스 예외 추상 클래스]
 *
 * 📌 역할 및 목적:
 * - 모든 도메인(비즈니스) 예외들의 **공통 부모** 역할을 수행
 * - 서비스 계층에서 발생하는 의도된 도메인 오류를 구분하기 위해 사용
 * - 예외 발생 시, 자동으로 로깅 처리되며 추후 통일된 예외 응답 구조 확장 가능
 *
 * ✅ 사용 예시:
 * - LoginFailedException extends BusinessException
 * - DuplicateUsernameException extends BusinessException
 *
 * ✅ 장점:
 * - 예외를 하나의 범주로 묶어 @ExceptionHandler(BusinessException.class) 등에서 일괄 처리 가능
 * - 도메인 로직과 시스템 예외를 구분하여 의미 있는 예외 설계 가능
 * - 자동 로그 기록 (Logger 활용)
 */
public abstract class BusinessException extends RuntimeException {

    private static final Logger log = LoggerFactory.getLogger(BusinessException.class);

    /**
     * 공통 생성자
     * @param message 클라이언트 또는 로깅 용도의 예외 메시지
     */
    public BusinessException(String message) {
        super(message);
        log.warn("[BusinessException 발생] {}", message);
    }

    // ✅ 향후 확장 고려 필드 예시
    // private final int errorCode;
    // private final HttpStatus status;
    // → 에러 응답 통일을 위한 DTO 구성에 활용 가능
}
