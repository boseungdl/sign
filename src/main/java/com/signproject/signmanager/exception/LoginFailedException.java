package com.signproject.signmanager.exception;

import com.signproject.signmanager.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

/**
 * [로그인 실패 예외]
 *
 * ✅ 발생 조건:
 * - 사용자 인증 실패 (아이디 불일치 또는 비밀번호 불일치)
 *
 * ✅ 처리 위치:
 * - AuthService에서 검증 실패 시 throw
 *
 * ✅ 특징:
 * - 상태 코드: 401 Unauthorized
 */
public class LoginFailedException extends BusinessException {

    /**
     * 로그인 실패 예외 생성자
     *
     * @param message 클라이언트 또는 로그용 메시지
     */
    public LoginFailedException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}
