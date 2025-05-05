package com.milvus.vector_spring.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProjectUpdateRequestDto {
    private String name;
    private String openAiKey;
    private String prompt;
    private String embedModel;
    private String chatModel;
    private long dimensions;

    @NotNull
    @NotBlank
    private Long updatedUserId;
}
