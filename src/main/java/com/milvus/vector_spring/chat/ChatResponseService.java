package com.milvus.vector_spring.chat;

import com.milvus.vector_spring.chat.dto.ChatRequestDto;
import com.milvus.vector_spring.chat.dto.ChatResponseDto;
import com.milvus.vector_spring.chat.dto.VectorSearchRankDto;
import com.milvus.vector_spring.content.Content;
import io.milvus.v2.service.vector.response.SearchResp;

import java.time.LocalDateTime;
import java.util.List;

public interface ChatResponseService {
    ChatResponseDto buildResponse(ChatRequestDto requestDto, String finalAnswer, LocalDateTime inputTime, LocalDateTime outputTime, Content content, List<VectorSearchRankDto> rankList, SearchResp searchResults);
    void saveResponse(ChatRequestDto requestDto, String finalAnswer, LocalDateTime inputTime, LocalDateTime outputTime, Content content, List<VectorSearchRankDto> rankList);
}
