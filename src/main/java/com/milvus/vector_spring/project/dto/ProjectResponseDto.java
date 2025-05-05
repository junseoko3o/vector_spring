package com.milvus.vector_spring.project.dto;

import com.milvus.vector_spring.project.Project;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ProjectResponseDto {

    private final Long id;
    private final String name;
    private final String key;
    private String prompt;
    private String embedModel;
    private String chatModel;
    private long dimensions;
    private final Long createdUserId;
    private final Long updatedUserId;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static ProjectResponseDto projectResponseDto(Project project) {
        return ProjectResponseDto.builder()
                .id(project.getId())
                .name(project.getName())
                .key(project.getKey())
                .prompt(project.getPrompt())
                .embedModel(project.getEmbedModel())
                .chatModel(project.getChatModel())
                .dimensions(project.getDimensions())
                .createdUserId(project.getCreatedBy().getId())
                .updatedUserId(project.getUpdatedBy().getId())
                .createdAt(project.getCreatedAt())
                .updatedAt(project.getUpdatedAt())
                .build();
    }
}
