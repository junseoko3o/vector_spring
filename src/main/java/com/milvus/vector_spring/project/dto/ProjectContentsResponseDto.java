package com.milvus.vector_spring.project.dto;

import com.milvus.vector_spring.content.dto.ContentResponseDto;
import com.milvus.vector_spring.project.Project;
import com.milvus.vector_spring.user.User;

import java.time.LocalDateTime;
import java.util.List;

public class ProjectContentsResponseDto {
    private Long id;
    private String name;
    private String key;
    private String openAiKey;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<ContentResponseDto> contents;

    public ProjectContentsResponseDto(Project project) {
        this.id = project.getId();
        this.name = project.getName();
        this.key = project.getKey();
        this.openAiKey = project.getOpenAiKey();
        this.createdAt = project.getCreatedAt();
        this.updatedAt = project.getUpdatedAt();
        this.contents = project.getContents().stream()
                .map(ContentResponseDto::of)
                .toList();
    }
}
