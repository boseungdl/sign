package com.signproject.signmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * [유저 응답 DTO]
 * - 로그인, 내 정보 조회, 관리자 유저 리스트 등에서 공통 사용 가능
 */
@Getter
@AllArgsConstructor
public class UserResponseDto {
    private Long id;
    private String username;
    private String role;
}
