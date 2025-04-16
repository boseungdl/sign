package com.signproject.signmanager.common.annotation;

import java.lang.annotation.*;

/**
 * [@Login 사용자 주입 어노테이션]
 * - 로그인한 유저 정보를 컨트롤러 메서드 파라미터에 주입할 때 사용
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Login {
}
