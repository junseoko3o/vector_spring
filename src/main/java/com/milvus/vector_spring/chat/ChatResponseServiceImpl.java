package com.milvus.vector_spring.chat;

import com.milvus.vector_spring.chat.dto.ChatRequestDto;
import com.milvus.vector_spring.chat.dto.ChatResponseDto;
import com.milvus.vector_spring.chat.dto.VectorSearchRankDto;
import com.milvus.vector_spring.config.mongo.document.ChatResponseDocument;
import com.milvus.vector_spring.content.Content;
import io.milvus.v2.service.vector.response.SearchResp;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatResponseServiceImpl implements ChatResponseService {

    private final MongoTemplate mongoTemplate;

    @Override
    public ChatResponseDto buildResponse(ChatRequestDto requestDto, String finalAnswer, LocalDateTime inputTime, LocalDateTime outputTime, Content content, List<VectorSearchRankDto> rankList, SearchResp searchResults) {
        requestDto.setSessionId("DEV");
        return ChatResponseDto.chatResponseDto(
                requestDto.getProjectKey(),
                requestDto.getSessionId(),
                requestDto.getText(),
                finalAnswer,
                inputTime,
                outputTime,
                searchResults,
                content
        );
    }

    @Override
    public void saveResponse(ChatRequestDto requestDto, String finalAnswer, LocalDateTime inputTime, LocalDateTime outputTime, Content content, List<VectorSearchRankDto> rankList) {
        ChatResponseDocument.SimpleContentDto simpleContent = Optional.ofNullable(content)
                .map(c -> new ChatResponseDocument.SimpleContentDto(
                        c.getId(),
                        c.getKey(),
                        c.getTitle(),
                        c.getAnswer()
                ))
                .orElse(new ChatResponseDocument.SimpleContentDto(null, null, null, null));
        ChatResponseDocument doc = new ChatResponseDocument(
                requestDto.getSessionId(),
                requestDto.getText(),
                finalAnswer,
                inputTime,
                outputTime,
                simpleContent,
                rankList
        );
        mongoTemplate.save(doc);
    }
}
