package com.milvus.vector_spring.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserLoginRequestDto {
    @Email(message = "이메일 형식 이어야 한다.")
    private String email;

    @NotBlank
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$",
            message = "비밀번호는 숫자, 문자, 특수문자 1개 이상 ")
    private String password;
    private LocalDateTime loginAt = LocalDateTime.now();
}
