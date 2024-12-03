package com.milvus.vector_spring.project.dto;

import com.milvus.vector_spring.project.Project;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
@Getter
@RequiredArgsConstructor
public class ProjectResponseDto {

    private final Long id;
    private final String name;
    private final String key;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static ProjectResponseDto of (Project project) {
        return new ProjectResponseDto(
                project.getId(),
                project.getName(),
                project.getKey(),
                project.getCreatedAt(),
                project.getUpdatedAt()
        );
    }
}
