package com.milvus.vector_spring.content.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ContentCreateRequestDto {
    @NotNull(message = "필수 입력")
    @NotBlank
    private String title;

    private String answer;

    @NotBlank
    @NotNull(message = "필수 입력")
    private String projectKey;
}
