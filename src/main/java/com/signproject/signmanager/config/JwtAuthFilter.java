package com.signproject.signmanager.config;

import com.signproject.signmanager.common.exception.BusinessException;
import com.signproject.signmanager.common.exception.InvalidTokenException;
import com.signproject.signmanager.common.exception.NoTokenException;
import com.signproject.signmanager.util.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * [JWT 인증 필터]
 * - 매 요청마다 Authorization 헤더에서 JWT 토큰을 추출하고
 * - 유효한 경우 인증 객체(SecurityContext)에 등록
 * - 특정 경로는 필터 생략 (shouldNotFilter)
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    // 1. 필터를 적용하지 않을 경로 (화이트리스트)
    private static final List<String> EXCLUDE_URLS = List.of(
            "/api/auth/login",
            "/api/auth/register",
            "/swagger-ui", // 문서 경로 등도 예외 처리 가능
            "/v3/api-docs"
    );

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return EXCLUDE_URLS.stream().anyMatch(path::startsWith);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String token = jwtTokenProvider.resolveToken(request);

        if (token == null) {
            throw new NoTokenException();
        }

        if (!jwtTokenProvider.validateToken(token)) {
            throw new InvalidTokenException();
        }

        Long userId = jwtTokenProvider.getUserIdFromToken(token);

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(userId, null, null);

        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);


        // ✅ 요청 속성에 저장 (ArgumentResolver 전달용)
        request.setAttribute("userId", userId);

        filterChain.doFilter(request, response);
    }
}
