package com.milvus.vector_spring.openai.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OpenAiChatRequestDto {
    private String model = "gpt-4o";
    private List<OpenAiMessageDto> messages;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OpenAiMessageDto {
        private String role;
        private String content;
    }
}