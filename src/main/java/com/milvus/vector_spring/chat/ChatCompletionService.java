package com.milvus.vector_spring.chat;

import com.milvus.vector_spring.chat.dto.VectorSearchRankDto;
import com.milvus.vector_spring.chat.dto.VectorSearchResponseDto;
import com.openai.models.chat.completions.ChatCompletion;

import java.util.List;

public interface ChatCompletionService {
    ChatCompletion generateAnswer(String chatModel, String userText, String openAiKey, List<VectorSearchRankDto> rankList, VectorSearchResponseDto searchResponse, String prompt);
}
