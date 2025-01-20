package com.milvus.vector_spring.openai.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OpenAiChatRequestDto {
    private String model;

    @NotNull
    private List<OpenAiMessageDto> messages;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OpenAiMessageDto {
        private String role;
        private String content;
    }
}