package com.milvus.vector_spring.openai.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OpenAiChatResponseDto {
    private final String id;
    private final String object;
    private final Long created;
    private final String model;
    private final List<Choice> choices;
    private final OpenAiUsageResponseDto usage;
    private final String systemFingerprint;

    public OpenAiChatResponseDto(
            @JsonProperty("id") String id,
            @JsonProperty("object") String object,
            @JsonProperty("created") Long created,
            @JsonProperty("model") String model,
            @JsonProperty("choices") List<Choice> choices,
            @JsonProperty("usage") OpenAiUsageResponseDto usage,
            @JsonProperty("system_fingerprint") String systemFingerprint
    ) {
        this.id = id;
        this.object = object;
        this.created = created;
        this.model = model;
        this.choices = choices;
        this.usage = usage;
        this.systemFingerprint = systemFingerprint;
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Choice {
        private final int index;
        private final Message message;
        private final Object logprobs; // JSON 구조상 null 가능
        private final String finishReason;

        public Choice(
                @JsonProperty("index") int index,
                @JsonProperty("message") Message message,
                @JsonProperty("logprobs") Object logprobs,
                @JsonProperty("finish_reason") String finishReason
        ) {
            this.index = index;
            this.message = message;
            this.logprobs = logprobs;
            this.finishReason = finishReason;
        }

        @Getter
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Message {
            private final String role;
            private final String content;
            private final Object refusal;

            public Message(
                    @JsonProperty("role") String role,
                    @JsonProperty("content") String content,
                    @JsonProperty("refusal") Object refusal
            ) {
                this.role = role;
                this.content = content;
                this.refusal = refusal;
            }
        }
    }
}
