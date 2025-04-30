package com.signproject.signmanager.config;

import com.signproject.signmanager.common.resolver.LoginUserArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.*;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final LoggingInterceptor loggingInterceptor;
    private final LoginUserArgumentResolver loginUserArgumentResolver;

    /**
     * 모든 API 요청에 대해 로깅 인터셉터 적용
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loggingInterceptor)
                .order(Ordered.HIGHEST_PRECEDENCE) // 무슨 코드지
                .addPathPatterns("/**");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        // 커스텀 어노테이션을 통해 로그인 유저 자동 주입
        resolvers.add(loginUserArgumentResolver);
    }
    /**
     * CORS 설정 (프론트 React 연결 대비)
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("*")
                .allowCredentials(true);
    }

}
