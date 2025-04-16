package com.signproject.signmanager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 로그인 요청 시 클라이언트로부터 전달받는 데이터 구조
 * - 실무 수준의 입력 검증 적용
 */
@Getter
@NoArgsConstructor
public class LoginRequestDto {

    @NotBlank(message = "아이디는 필수입니다.")
    @Size(min = 5, max = 20, message = "아이디는 5자 이상 20자 이하여야 합니다.")
    @Pattern(
            regexp = "^[a-zA-Z0-9]+$",
            message = "아이디는 영문자와 숫자만 사용할 수 있습니다."
    )
    private String username;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하여야 합니다.")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+=-]).{8,20}$",
            message = "비밀번호는 영문, 숫자, 특수문자를 모두 포함해야 합니다."
    )
    private String password;
}
