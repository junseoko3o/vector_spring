package com.milvus.vector_spring.openai.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class EmbedRequestDto {
    @NotNull
    private String embedText;

    @Builder
    public EmbedRequestDto(String embedText) {
        this.embedText = embedText;
    }
}
