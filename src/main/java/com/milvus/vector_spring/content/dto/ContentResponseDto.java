package com.milvus.vector_spring.content.dto;

import com.milvus.vector_spring.content.Content;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ContentResponseDto {
    private final Long id;
    private final String title;
    private final String answer;
    private final Long projectId;
    private final Long createdUserId;
    private final Long updatedUserId;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static ContentResponseDto contentResponseDto(Content content) {
        return ContentResponseDto.builder()
                .id(content.getId())
                .title(content.getTitle())
                .answer(content.getAnswer())
                .projectId(content.getProject().getId())
                .createdUserId(content.getCreatedBy().getId())
                .updatedUserId(content.getUpdatedBy().getId())
                .createdAt(content.getCreatedAt())
                .updatedAt(content.getUpdatedAt())
                .build();
    }

    @Builder
    public ContentResponseDto(Long id, String title, String answer, Long projectId, Long createdUserId, Long updatedUserId, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.answer = answer;
        this.projectId = projectId;
        this.createdUserId = createdUserId;
        this.updatedUserId = updatedUserId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
