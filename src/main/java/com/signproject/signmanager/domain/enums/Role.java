package com.signproject.signmanager.domain.enums;

import lombok.Getter;


/**
 * [사용자 권한 Enum]
 * - 사용자(Role.USER), 관리자(Role.ADMIN) 등 권한을 구분할 때 사용
 * - DB에는 문자열("USER", "ADMIN")로 저장됨
 * - 사용자 인증 및 인가 시 역할 확인용
 */
@Getter
public enum Role {
    USER,   // 일반 사용자
    ADMIN   // 관리자
}
