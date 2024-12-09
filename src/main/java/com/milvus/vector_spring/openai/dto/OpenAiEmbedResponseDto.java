package com.milvus.vector_spring.openai.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OpenAiEmbedResponseDto {
    private final String object;
    private final String model;
    private final List<Data> data;
    private final OpenAiUsageResponseDto usage;

    public OpenAiEmbedResponseDto(
            @JsonProperty("object") String object,
            @JsonProperty("model") String model,
            @JsonProperty("data") List<Data> data,
            @JsonProperty("usage") OpenAiUsageResponseDto usage
    ) {
        this.object = object;
        this.model = model;
        this.data = data;
        this.usage = usage;
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {
        private final String object;
        private final int index;
        private final List<Double> embedding;

        public Data(
                @JsonProperty("object") String object,
                @JsonProperty("index") int index,
                @JsonProperty("embedding") List<Double> embedding
        ) {
            this.object = object;
            this.index = index;
            this.embedding = embedding;
        }
    }
}
