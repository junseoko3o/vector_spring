package com.milvus.vector_spring.user.dto;

import com.milvus.vector_spring.content.dto.ContentResponseDto;
import com.milvus.vector_spring.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class UserContentsResponseDto {
    private Long id;
    private String email;
    private String username;
    private LocalDateTime loginAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<ContentResponseDto> contents;

    public UserContentsResponseDto(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.loginAt = user.getLoginAt();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();
        this.contents = user.getCreatedUser().stream()
                .map(ContentResponseDto::of)
                .toList();
    }
}