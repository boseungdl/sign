package com.signproject.signmanager.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 로그인 요청 시 클라이언트로부터 전달받는 데이터 구조
 */
@Getter
@NoArgsConstructor
public class LoginRequestDto {

    private String username;
    private String password;

    // validation, constructor, builder 등은 필요 시 확장 가능
}
