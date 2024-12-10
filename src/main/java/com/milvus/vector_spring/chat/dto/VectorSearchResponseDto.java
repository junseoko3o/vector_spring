package com.milvus.vector_spring.chat.dto;

import io.milvus.v2.service.vector.response.SearchResp;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class VectorSearchResponseDto {
    private SearchResp search;
    private Long firstSearchId;
    private List<String> answers;
}
