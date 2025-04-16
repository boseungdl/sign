// 📁 경로: src/main/java/com/signproject/signmanager/dto/LoginResponseDto.java

package com.signproject.signmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * [로그인 성공 응답 DTO]
 * - 토큰 + 로그인한 사용자 정보 반환
 */
@Getter
@AllArgsConstructor
public class LoginResponseDto {
    private String token;
    private UserInfoDto user;
}
