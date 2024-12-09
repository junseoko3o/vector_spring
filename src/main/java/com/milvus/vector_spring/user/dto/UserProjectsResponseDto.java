package com.milvus.vector_spring.user.dto;

import com.milvus.vector_spring.project.dto.ProjectResponseDto;
import com.milvus.vector_spring.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class UserProjectsResponseDto {
    private Long id;
    private String email;
    private String username;
    private LocalDateTime loginAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<ProjectResponseDto> projects;

    public UserProjectsResponseDto(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.loginAt = user.getLoginAt();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();
        this.projects = user.getCreatedProjectUser().stream()
                .map(ProjectResponseDto::projectResponseDto)
                .toList();
    }

    public static UserProjectsResponseDto of(User user) {
        return new UserProjectsResponseDto(user);
    }
}