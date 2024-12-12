package com.milvus.vector_spring.content.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ContentUpdateRequestDto {
    private String title;
    private String answer;

    @NotNull(message = "필수 입력")
    private Long updatedUserId;
}
