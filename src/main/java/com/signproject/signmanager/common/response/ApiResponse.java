package com.signproject.signmanager.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * [공통 API 응답 포맷 클래스]
 * - 모든 API 응답을 통일된 구조로 감싸기 위해 사용
 * - status, success, message, data 필드 제공
 *
 * ✅ 사용 예시:
 * return ApiResponse.success(HttpStatus.OK, "로그인 성공", userDto);
 * return ApiResponse.error(HttpStatus.BAD_REQUEST, "잘못된 요청입니다", null);
 */
@Getter
@AllArgsConstructor
public class ApiResponse<T> {
    private int status;         // HTTP 상태 코드
    private boolean success;    // 요청 성공 여부
    private String message;     // 메시지
    private T data;             // 응답 데이터

    /**
     * 성공 응답 생성 메서드
     * @param status HTTP 상태 코드 (예: HttpStatus.OK)
     * @param message 사용자에게 전달할 메시지
     * @param data 실제 응답 데이터
     */
    public static <T> ApiResponse<T> success(HttpStatus status, String message, T data) {
        return new ApiResponse<>(status.value(), true, message, data);
    }

    /**
     * 실패 응답 생성 메서드
     * @param status HTTP 상태 코드 (예: HttpStatus.BAD_REQUEST)
     * @param message 사용자에게 전달할 에러 메시지
     * @param data 예외 정보 등 (필요 없으면 null 가능)
     */
    public static <T> ApiResponse<T> error(HttpStatus status, String message, T data) {
        return new ApiResponse<>(status.value(), false, message, data);
    }
}
