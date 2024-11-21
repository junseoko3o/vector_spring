package com.milvus.vector_spring.user.dto;

import com.milvus.vector_spring.user.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class UserResponseDto {

    private final Long id;
    private final String email;
    private final String username;
    private final LocalDateTime loginAt;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static UserResponseDto of (User user) {
        return new UserResponseDto(
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                user.getLoginAt(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
