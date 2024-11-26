package com.milvus.vector_spring.openai.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
@Getter
@Builder
public class OpenAiEmbedResponseDto {
    private List<Float> embedding;
    private OpenAiUsageResponseDto usage;
}
