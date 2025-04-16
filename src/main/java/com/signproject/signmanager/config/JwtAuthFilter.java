package com.signproject.signmanager.config;

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

/**
 * [JWT 인증 필터]
 * - 매 요청마다 Authorization 헤더에서 JWT 토큰을 추출하고
 * - 유효한 경우 인증 객체(SecurityContext)에 등록
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String token = jwtTokenProvider.resolveToken(request);
        if (token != null && jwtTokenProvider.validateToken(token)) {
            Long userId = jwtTokenProvider.getUserIdFromToken(token);

            // 인증 객체 생성 및 SecurityContext 등록
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(userId, null, null); // 권한 필요시 마지막에 컬렉션 추가

            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        // 다음 필터로 진행
        filterChain.doFilter(request, response);
    }
}
