package com.signproject.signmanager.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

/**
 * JWT 토큰 생성 및 검증을 담당하는 유틸 클래스
 * - 로그인 성공 시 토큰 생성
 * - 인증 요청 시 토큰 파싱 및 유효성 검사 수행
 */
@Slf4j
@Component
public class JwtTokenProvider {

    private String secretKey = "sign-project-super-secret-key-should-be-long"; // TODO: 환경변수 처리 권장
    private Key key;
    private final long EXPIRATION_TIME = 1000L * 60 * 60 * 2; // 2시간 유효

    /**
     * 애플리케이션 초기화 시 Secret Key 인코딩 및 키 생성
     */
    @PostConstruct
    public void init() {
        byte[] keyBytes = Base64.getEncoder().encode(secretKey.getBytes());
        this.key = Keys.hmacShaKeyFor(keyBytes);
        log.info("🔑 [JwtTokenProvider] JWT 키 초기화 완료");
    }

    /**
     * JWT 토큰 생성
     *
     * @param userId 사용자 ID
     * @return JWT 문자열
     */
    public String createToken(Long userId) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + EXPIRATION_TIME);

        String token = Jwts.builder()
                .setSubject(String.valueOf(userId)) // 토큰 subject에 userId 저장
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        log.info("🎟️ [createToken] 사용자 ID {}에 대한 토큰 생성 완료", userId);
        return token;
    }

    /**
     * 요청 헤더에서 JWT 토큰을 추출하는 메서드
     *
     * @param request HttpServletRequest
     * @return 추출된 토큰 값 (없으면 null)
     */
    public String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        log.debug("📥 [resolveToken] Authorization 헤더 값: {}", bearer);
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }

    /**
     * 토큰 유효성 검증
     *
     * @param token 클라이언트로부터 받은 토큰
     * @return 유효하면 true, 그렇지 않으면 false
     */
    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            log.debug("✅ [validateToken] 유효한 토큰입니다.");
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("❌ [validateToken] 토큰 검증 실패: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 토큰에서 사용자 ID 추출
     *
     * @param token JWT 문자열
     * @return 사용자 ID
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = parseClaims(token);
        log.debug("👤 [getUserIdFromToken] 파싱된 사용자 ID: {}", claims.getSubject());
        return Long.parseLong(claims.getSubject());
    }

    /**
     * 내부적으로 클레임 파싱 (만료 검증 포함)
     *
     * @param token JWT 문자열
     * @return Claims 객체
     */
    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
