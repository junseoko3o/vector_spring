package com.milvus.vector_spring.project.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProjectDeleteRequestDto {
    private String key;
    private Long userId;
}
