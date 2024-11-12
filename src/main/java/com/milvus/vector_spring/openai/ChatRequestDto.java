package com.milvus.vector_spring.openai;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ChatRequestDto {
    private final String role;
    private final String content;
}
