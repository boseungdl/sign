package com.signproject.signmanager.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * [공통 에러 응답 DTO]
 * - 예외 발생 시 클라이언트에게 에러 상태와 메시지를 JSON 형태로 전달
 * - 예: {"status":400, "errors":["비밀번호는 필수입니다."]}
 */
@Getter
@AllArgsConstructor
public class ApiErrorResponse {

    /** HTTP 상태 코드 */
    private int status;

    /** 오류 메시지 목록 */
    private List<String> errors;
}
