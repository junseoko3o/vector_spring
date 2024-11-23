package com.milvus.vector_spring.openai.dto;


import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;

@Getter
public class EmbedResponseDto {
    private JsonNode embedding;
    private JsonNode usage;

    public EmbedResponseDto(JsonNode embedding, JsonNode usage) {
        this.embedding = embedding;
        this.usage = usage;
    }
}
