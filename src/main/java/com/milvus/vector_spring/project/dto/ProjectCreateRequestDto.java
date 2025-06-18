package com.milvus.vector_spring.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProjectCreateRequestDto {
    @NotNull(message = "필수 입력")
    @NotBlank
    private String name;

    private String openAiKey;

    private String embedModel;

    private String chatModel;

    private String prompt;

    private long dimensions;

    @NotNull
    private Long createdUserId;
}
