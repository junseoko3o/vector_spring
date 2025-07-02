package com.milvus.vector_spring.milvus;

import com.milvus.vector_spring.chat.dto.VectorSearchRankDto;
import com.milvus.vector_spring.chat.dto.VectorSearchResponseDto;
import com.openai.models.embeddings.CreateEmbeddingResponse;

import java.util.List;

public interface VectorSearchService {
    CreateEmbeddingResponse createEmbedding(String openAiKey, String text, long dimensions);
    VectorSearchResponseDto searchVector(CreateEmbeddingResponse embedding, Long projectId);
    List<VectorSearchRankDto> convertToRankList(VectorSearchResponseDto searchResponse);
}