package com.signproject.signmanager.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 사용자 정보를 담는 JPA Entity
 * - 로그인 시 username/password 검증용
 */
@Entity
@Getter
@NoArgsConstructor
@Table(name = "users") // 테이블명 명시
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username; // 로그인 ID

    @Column(nullable = false)
    private String password; // 비밀번호 (암호화 저장 예정)

    @Column
    private String role = "USER"; // 기본 권한

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
