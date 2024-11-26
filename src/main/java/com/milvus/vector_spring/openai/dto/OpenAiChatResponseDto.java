package com.milvus.vector_spring.openai.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OpenAiChatResponseDto {
    private String output;
    private OpenAiUsageResponseDto usage;
}
