package com.milvus.vector_spring.openai.dto;

import lombok.Builder;
import lombok.Getter;
@Getter
@Builder
public class OpenAiUsageResponseDto {
    private int prompt_tokens;
    private int completion_tokens;
    private int total_tokens;
}
