// 커밋 메시지: "JwtAuthFilter에 LogTrace 및 SecurityContext 통합 적용"
// 📄 src/main/java/com/signproject/signmanager/config/JwtAuthFilter.java

package com.signproject.signmanager.config;

import com.signproject.signmanager.common.exception.InvalidTokenException;
import com.signproject.signmanager.common.exception.NoTokenException;
import com.signproject.signmanager.common.trace.LogTrace;
import com.signproject.signmanager.common.trace.TraceStatus;
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
 * - 모든 요청에 대해 Authorization 헤더의 JWT 검증
 * - 유효 토큰인 경우 SecurityContext에 인증 정보 등록
 * - LogTrace를 사용해 필터 단계부터 일관된 트레이스 유지
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final LogTrace trace;

    // 필터를 적용하지 않을 엔드포인트 목록 (ContextPath 제외)
    private static final List<String> EXCLUDE_URLS = List.of(
            "/api/auth/", "/swagger-ui/", "/v3/api-docs/"
    );

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return EXCLUDE_URLS.stream().anyMatch(path::startsWith);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        // 1) 필터 진입 시 트레이스 시작
        TraceStatus status = trace.begin("[Filter] JWT 인증");
        try {
            // 2) 토큰 추출 및 검증
            String token = jwtTokenProvider.resolveToken(request);
            if (token == null) {
                throw new NoTokenException();
            }
            if (!jwtTokenProvider.validateToken(token)) {
                throw new InvalidTokenException();
            }

            // 3) 사용자 정보 파싱
            Long userId = jwtTokenProvider.getUserIdFromToken(token);

            // 4) 인증 객체 생성 및 SecurityContext에 등록
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(userId, null, List.of());
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);

            // 5) ArgumentResolver 등에서 사용할 userId 속성 저장
            request.setAttribute("userId", userId);

            // 6) 다음 필터 체인 실행
            filterChain.doFilter(request, response);

            // 7) 정상 완료 트레이스 종료
            trace.end(status);
        } catch (Exception ex) {
            // 8) 예외 시 트레이스 예외 기록 후 다시 던짐
            trace.exception(status, ex);
            throw ex;
        }
    }
}
