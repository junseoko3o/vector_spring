package com.milvus.vector_spring.project.dto;

import lombok.Getter;

@Getter
public class ProjectUpdateRequestDto {
    private String name;
    private String openAiKey;
    private Long updatedUserId;
}
