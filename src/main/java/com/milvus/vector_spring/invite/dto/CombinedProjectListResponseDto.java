package com.milvus.vector_spring.invite.dto;

import com.milvus.vector_spring.project.Project;
import com.milvus.vector_spring.project.dto.ProjectResponseDto;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CombinedProjectListResponseDto {
    private final Long id;
    private final boolean mine;
    private final String name;
    private final String key;
    private final Long createdUserId;
    private final Long updatedUserId;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static CombinedProjectListResponseDto CombinedProjectListResponseDto(Project project, boolean mine) {
        return CombinedProjectListResponseDto.builder()
                .id(project.getId())
                .name(project.getName())
                .mine(mine)
                .key(project.getKey())
                .createdUserId(project.getCreatedBy().getId())
                .updatedUserId(project.getUpdatedBy().getId())
                .createdAt(project.getCreatedAt())
                .updatedAt(project.getUpdatedAt())
                .build();
    }
}
