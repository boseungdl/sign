package com.signproject.signmanager.common.exhandler;

import com.signproject.signmanager.common.response.ApiResponse;
import com.signproject.signmanager.common.trace.LogTrace;
import com.signproject.signmanager.common.trace.TraceStatus;
import com.signproject.signmanager.common.exception.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.security.auth.message.AuthException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * [전역 예외 핸들러]
 * - @Valid 유효성 검증, 커스텀 예외 등 전역 예외 관리
 * - Filter/Interceptor에서 시작된 TraceStatus 참조해 일관된 traceId 유지
 */
@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final LogTrace trace; // LogTrace 주입

    /**
     * 📌 JSON 파싱 실패 or 타입 불일치
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<?>> handleJsonParseException(
            HttpServletRequest request,
            HttpMessageNotReadableException ex) {
        TraceStatus status = (TraceStatus) request.getAttribute("traceStatus");
        if (status != null) trace.exception(status, ex);
        return ResponseEntity.badRequest()
                .body(ApiResponse.error(HttpStatus.BAD_REQUEST,
                        "요청 형식이 올바르지 않거나 데이터 타입이 잘못되었습니다.", null));
    }

    /**
     * 📌 @Valid - @RequestBody 입력값 검증 실패
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidationException(
            HttpServletRequest request,
            MethodArgumentNotValidException ex) {
        TraceStatus status = (TraceStatus) request.getAttribute("traceStatus");
        if (status != null) trace.exception(status, ex);
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
        return ResponseEntity.badRequest()
                .body(ApiResponse.error(HttpStatus.BAD_REQUEST,
                        "입력값 검증 실패", errors));
    }

    /**
     * 📌 @RequestParam, @PathVariable 등 검증 실패
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<?>> handleConstraintViolation(
            HttpServletRequest request,
            ConstraintViolationException ex) {
        TraceStatus status = (TraceStatus) request.getAttribute("traceStatus");
        if (status != null) trace.exception(status, ex);
        List<String> errors = ex.getConstraintViolations()
                .stream().map(v -> v.getMessage()).toList();
        return ResponseEntity.badRequest()
                .body(ApiResponse.error(HttpStatus.BAD_REQUEST,
                        "제약 조건 위반", errors));
    }

    /**
     * 📌 @ModelAttribute 바인딩 실패
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ApiResponse<?>> handleBindException(
            HttpServletRequest request,
            BindException ex) {
        TraceStatus status = (TraceStatus) request.getAttribute("traceStatus");
        if (status != null) trace.exception(status, ex);
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
        return ResponseEntity.badRequest()
                .body(ApiResponse.error(HttpStatus.BAD_REQUEST,
                        "바인딩 실패", errors));
    }

    /**
     * 📌 인증 실패 관련 예외 처리
     */
    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ApiResponse<?>> handleAuthException(
            HttpServletRequest request,
            AuthException ex) {
        TraceStatus status = (TraceStatus) request.getAttribute("traceStatus");
        if (status != null) trace.exception(status, ex);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error(HttpStatus.UNAUTHORIZED,
                        ex.getMessage(), null));
    }

    /**
     * 📌 비즈니스 로직 오류 처리
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<?>> handleBusinessException(
            HttpServletRequest request,
            BusinessException ex) {
        TraceStatus status = (TraceStatus) request.getAttribute("traceStatus");
        if (status != null) trace.exception(status, ex);

        return ResponseEntity.status(ex.getStatus())
                .body(ApiResponse.error(
                        ex.getStatus(),       // HTTP 상태
                        ex.getMessage(),      // 예외 메시지
                        null                  // 데이터 없음
                ));
    }

    /**
     * 📌 예상치 못한 서버 오류 처리
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleGlobalException(
            HttpServletRequest request,
            Exception ex) {
        TraceStatus status = (TraceStatus) request.getAttribute("traceStatus");
        if (status != null) trace.exception(status, ex);
        log.error("[ServerError] 처리되지 않은 예외 발생", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR,
                        "서버 내부 오류가 발생했습니다.", null));
    }
}
