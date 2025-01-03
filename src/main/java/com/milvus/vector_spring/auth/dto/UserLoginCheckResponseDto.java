package com.milvus.vector_spring.auth.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserLoginCheckResponseDto {
    private Long id;
    private String email;
    private String accessToken;
    private LocalDateTime loginAt;
}
