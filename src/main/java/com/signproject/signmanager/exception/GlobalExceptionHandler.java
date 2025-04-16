// src/main/java/com/signproject/signmanager/exception/GlobalExceptionHandler.java

package com.signproject.signmanager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 프로젝트 전역에서 발생하는 예외를 처리하는 핸들러
 * Rest API용으로 HTTP 상태 코드와 메시지 반환
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(LoginFailedException.class)
    public ResponseEntity<String> handleLoginFailed(LoginFailedException e) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(e.getMessage()); // 클라이언트에게 메시지 전달
    }

    // TODO: 다른 예외 처리 추가 가능
}
