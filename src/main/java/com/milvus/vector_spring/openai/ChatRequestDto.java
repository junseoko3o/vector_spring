package com.milvus.vector_spring.openai;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatRequestDto {
    private String role;
    private String content;
}
