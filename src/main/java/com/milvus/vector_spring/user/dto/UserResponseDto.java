package com.milvus.vector_spring.user.dto;

import com.milvus.vector_spring.user.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserResponseDto {

    private final Long id;
    private final String email;
    private final String username;
    private final String role;
    private final LocalDateTime loginAt;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static UserResponseDto userResponseDto(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .role(user.getRole())
                .loginAt(user.getLoginAt())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
