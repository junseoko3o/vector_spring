package com.milvus.vector_spring.openai.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenAiUsageResponseDto {
    @JsonProperty("prompt_tokens")
    private final int promptTokens;

    @JsonProperty("completion_tokens")
    private final int completionTokens;

    @JsonProperty("total_tokens")
    private final int totalTokens;

    public OpenAiUsageResponseDto(
            @JsonProperty("prompt_tokens") int promptTokens,
            @JsonProperty("completion_tokens") int completionTokens,
            @JsonProperty("total_tokens") int totalTokens
    ) {
        this.promptTokens = promptTokens;
        this.completionTokens = completionTokens;
        this.totalTokens = totalTokens;
    }
}
