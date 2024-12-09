package com.milvus.vector_spring.project.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProjectUpdateRequestDto {
    private String name;
    private String openAiKey;

    @NotNull
    private Long updatedUserId;
}
