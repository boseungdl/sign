package com.signproject.signmanager.common.resolver;

import com.signproject.signmanager.common.annotation.Login;
import com.signproject.signmanager.domain.User;
import com.signproject.signmanager.dto.UserInfoDto;
import com.signproject.signmanager.repository.UserRepository;
import com.signproject.signmanager.util.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.NoSuchElementException;

/**
 * [@Login 어노테이션 처리용 ArgumentResolver]
 * - 요청의 JWT에서 userId를 추출 후, 해당 유저 정보를 컨트롤러 파라미터로 주입
 */
@Component
@RequiredArgsConstructor
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // @Login 어노테이션이 있고, User 타입인 경우만 처리
        return parameter.hasParameterAnnotation(Login.class) &&
                parameter.getParameterType().equals(UserInfoDto.class); // ✅ DTO 타입 지원
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  org.springframework.web.bind.support.WebDataBinderFactory binderFactory) {
        HttpServletRequest request = ((ServletWebRequest) webRequest).getRequest();
        String token = tokenProvider.resolveToken(request);

        Long userId = tokenProvider.getUserIdFromToken(token);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("유저 정보를 찾을 수 없습니다."));

        return new UserInfoDto(
                user.getId(),
                user.getUsername(),
                user.getRole()
        );
    }
}
