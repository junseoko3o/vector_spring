package com.milvus.vector_spring.project.dto;

import lombok.Getter;

import java.util.Optional;

@Getter
public class ProjectCreateRequestDto {
    private String name;
    private Optional<String> openAiKey;
    private Long createdUserId;
}
