package com.milvus.vector_spring.auth.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserLoginResponseDto {
    private Long id;
    private String email;
    private String username;
    private String accessToken;
    private LocalDateTime loginAt;

    public UserLoginResponseDto(Long id, String email, String username, String accessToken, LocalDateTime loginAt) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.accessToken = accessToken;
        this.loginAt = loginAt;
    }
}
