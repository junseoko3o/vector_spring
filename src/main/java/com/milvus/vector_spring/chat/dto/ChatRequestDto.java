package com.milvus.vector_spring.chat.dto;

import lombok.Data;

@Data
public class ChatRequestDto {
    private String text;
    private String projectKey;
    private Long userId;
    private String sessionId;

}
