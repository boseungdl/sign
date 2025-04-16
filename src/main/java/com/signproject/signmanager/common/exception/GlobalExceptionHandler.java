package com.signproject.signmanager.common.exception;

import com.signproject.signmanager.common.response.ApiErrorResponse;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * [전역 예외 핸들러]
 * - @Valid 유효성 검증 실패, 커스텀 비즈니스 예외 등 프로젝트 전체의 예외를 통합 관리
 * - 예외에 따라 알맞은 상태 코드와 메시지를 포함한 표준 에러 응답 반환
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);



    /**
     * 📌 JSON 파싱 실패 or 타입 불일치 (ex: "username": true → String 필드에 boolean 입력)
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleJsonParseException(HttpMessageNotReadableException ex) {
        log.warn("[HttpMessageNotReadableException] JSON 파싱 실패 또는 타입 불일치", ex);
        return ResponseEntity.badRequest()
                .body(new ApiErrorResponse(HttpStatus.BAD_REQUEST.value(),
                        List.of("요청 형식이 올바르지 않거나 데이터 타입이 잘못되었습니다.")));
    }
    /**
     * 📌 @Valid - @RequestBody 입력값 검증 실패
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        log.warn("[ValidationException] 입력값 검증 실패: {}", errors);
        return ResponseEntity.badRequest()
                .body(new ApiErrorResponse(HttpStatus.BAD_REQUEST.value(), errors));
    }

    /**
     * 📌 @RequestParam, @PathVariable 등의 유효성 검증 실패
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleConstraintViolation(ConstraintViolationException ex) {
        List<String> errors = ex.getConstraintViolations()
                .stream()
                .map(v -> v.getMessage())
                .toList();

        log.warn("[ConstraintViolationException] 제약 조건 위반: {}", errors);
        return ResponseEntity.badRequest()
                .body(new ApiErrorResponse(HttpStatus.BAD_REQUEST.value(), errors));
    }

    /**
     * 📌 폼 객체 바인딩 실패 (ex: @ModelAttribute)
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ApiErrorResponse> handleBindException(BindException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        log.warn("[BindException] 바인딩 실패: {}", errors);
        return ResponseEntity.badRequest()
                .body(new ApiErrorResponse(HttpStatus.BAD_REQUEST.value(), errors));
    }

    /**
     * 📌 비즈니스 로직 오류 처리 (ex: 로그인 실패, 권한 없음 등)
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiErrorResponse> handleBusinessException(BusinessException ex) {
        log.warn("[BusinessException] {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ApiErrorResponse(HttpStatus.UNAUTHORIZED.value(), List.of(ex.getMessage())));
    }

    /**
     * 📌 예상치 못한 서버 오류 처리
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGlobalException(Exception ex) {
        log.error("[ServerError] 처리되지 않은 예외 발생", ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiErrorResponse(
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        List.of("서버 내부 오류가 발생했습니다."))
                );
    }
}
