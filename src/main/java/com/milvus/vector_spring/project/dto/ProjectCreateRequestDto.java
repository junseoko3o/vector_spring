package com.milvus.vector_spring.project.dto;

import lombok.Getter;

@Getter
public class ProjectCreateRequestDto {
    private String name;
    private String openAiKey;
    private Long createUserId;
}
