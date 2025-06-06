package com.milvus.vector_spring.project.dto;

import lombok.Data;

@Data
public class ProjectDeleteRequestDto {
    private String key;
    private Long userId;
}
