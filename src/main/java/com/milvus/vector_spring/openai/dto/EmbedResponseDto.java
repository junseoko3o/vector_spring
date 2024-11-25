package com.milvus.vector_spring.openai.dto;


import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;

import java.util.List;

@Getter
public class EmbedResponseDto {
    private List<Float> embedding;
    private JsonNode usage;

    public EmbedResponseDto(List<Float> embedding, JsonNode usage) {
        this.embedding = embedding;
        this.usage = usage;
    }
}
