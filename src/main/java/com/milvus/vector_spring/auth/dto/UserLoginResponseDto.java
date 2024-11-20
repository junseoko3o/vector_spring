package com.milvus.vector_spring.auth.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserLoginResponseDto {
    private Long id;
    private String email;
    private LocalDateTime loginAt;

    public UserLoginResponseDto(Long id, String email, LocalDateTime loginAt) {
        this.id = id;
        this.email = email;
        this.loginAt = loginAt;
    }
}
