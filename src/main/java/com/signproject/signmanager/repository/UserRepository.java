package com.signproject.signmanager.repository;

import com.signproject.signmanager.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * 사용자 정보를 조회하기 위한 JPA Repository
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * username 기준으로 사용자 조회
     * @param username 사용자 로그인 ID
     * @return Optional<User>
     */
    Optional<User> findByUsername(String username);
}
