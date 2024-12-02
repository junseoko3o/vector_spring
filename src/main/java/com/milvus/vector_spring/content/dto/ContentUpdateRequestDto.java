package com.milvus.vector_spring.content.dto;

import lombok.Getter;

@Getter
public class ContentUpdateRequestDto {
    private String title;
    private String answer;
    private Long updatedUserId;
}
