package com.signproject.signmanager.dto;

import com.signproject.signmanager.domain.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * [유저 응답 DTO]
 * - 로그인, 내 정보 조회, 관리자 유저 리스트 등에서 공통 사용 가능
 */
@Getter
@NoArgsConstructor
@Setter
@AllArgsConstructor
public class UserInfoDto {
    private Long id;
    private String username;
    private Role role;
}
