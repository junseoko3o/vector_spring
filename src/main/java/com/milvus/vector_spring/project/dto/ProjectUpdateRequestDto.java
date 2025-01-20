package com.milvus.vector_spring.project.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProjectUpdateRequestDto {
    private String name;
    private String openAiKey;
    private String prompt;
    private String embedModel;
    private String basicModel;
    private int dimensions;

    @NotNull
    private Long updatedUserId;
}
