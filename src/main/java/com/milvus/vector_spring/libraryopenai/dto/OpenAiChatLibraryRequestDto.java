package com.milvus.vector_spring.libraryopenai.dto;

import lombok.Getter;

@Getter
public class OpenAiChatLibraryRequestDto {
    private String content;
    private Role role;
    private String model;
}


