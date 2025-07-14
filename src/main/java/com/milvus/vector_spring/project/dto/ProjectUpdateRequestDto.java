package com.milvus.vector_spring.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
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
