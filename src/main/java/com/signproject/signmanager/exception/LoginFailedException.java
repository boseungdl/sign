// src/main/java/com/signproject/signmanager/exception/LoginFailedException.java

package com.signproject.signmanager.exception;

/**
 * 로그인 실패 시 던지는 커스텀 예외
 * (ex: 아이디 또는 비밀번호 불일치)
 */
public class LoginFailedException extends RuntimeException {
    public LoginFailedException(String message) {
        super(message);
    }
}
