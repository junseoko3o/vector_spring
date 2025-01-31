package com.milvus.vector_spring.libraryopenai.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
public class OpenAiChatLibraryRequestDto {
    private String openAiKey;
    @NotNull
    private List<OpenAiChatLibraryRequestDto.OpenAiLibaryMessageDto> messages;
    private String model;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OpenAiLibaryMessageDto {
        private String role;
        private String content;
    }
}


