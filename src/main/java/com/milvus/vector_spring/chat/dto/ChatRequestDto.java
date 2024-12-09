package com.milvus.vector_spring.chat.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatRequestDto {
    private String text;
    private String projectKey;
    private Long userId;
}
