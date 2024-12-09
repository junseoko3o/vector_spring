package com.milvus.vector_spring.chat.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class VectorSearchRankDto {
    private String answer;
    private String title;
    private double score;
    private Long id;
}
