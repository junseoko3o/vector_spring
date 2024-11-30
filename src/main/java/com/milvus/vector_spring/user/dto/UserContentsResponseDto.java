package com.milvus.vector_spring.user.dto;

import com.milvus.vector_spring.content.Content;
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
    private List<Content> contents;

    public UserContentsResponseDto(
            Long id, String email, String username,
            LocalDateTime loginAt, LocalDateTime createdAt,
            LocalDateTime updatedAt, List<Content> contents
    ) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.loginAt = loginAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.contents = contents;
    }
}
