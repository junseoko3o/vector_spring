package com.milvus.vector_spring.openai.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
@Getter
public class OpenAiEmbedResponseDto {
    private List<Float> embedding;
    private OpenAiUsageResponseDto usage;

    @Builder
    public OpenAiEmbedResponseDto(List<Float> embedding, OpenAiUsageResponseDto usage) {
        this.embedding = embedding;
        this.usage = usage;
    }
}
