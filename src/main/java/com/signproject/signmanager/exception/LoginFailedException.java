package com.signproject.signmanager.exception;

import com.signproject.signmanager.common.exception.BusinessException;

/**
 * [로그인 실패 예외]
 * - 사용자 인증 실패 시 (아이디/비밀번호 불일치 등) 발생
 *
 * 사용 예: AuthService에서 로그인 시 비밀번호 불일치 등 검증 실패 처리
 */
public class LoginFailedException extends BusinessException {

    /**
     * 생성자
     * @param message 실패 이유 (ex: 비밀번호가 틀렸습니다)
     */
    public LoginFailedException(String message) {
        super(message);
    }
}
