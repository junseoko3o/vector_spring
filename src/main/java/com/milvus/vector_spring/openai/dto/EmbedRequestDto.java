package com.milvus.vector_spring.openai.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
public class EmbedRequestDto {
    @NotNull
    private String embedText;

    @Builder
    public EmbedRequestDto(String embedText) {
        this.embedText = embedText;
    }
}
