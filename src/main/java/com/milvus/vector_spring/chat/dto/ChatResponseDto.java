package com.milvus.vector_spring.chat.dto;

import com.milvus.vector_spring.content.Content;
import io.milvus.v2.service.vector.response.SearchResp;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Builder
@Document(collection = "chat_response")
public class ChatResponseDto {
    private String sessionId;
    private String projectKey;
    private String input;
    private String output;
    private String vectorOutput;
    private Content content;
    private LocalDateTime inputDateTime;
    private LocalDateTime outputDateTime;
    private List<VectorSearchRankDto> rank;

    public static ChatResponseDto chatResponseDto(
            String projectKey, String sessionId,
            String input, String output,
            LocalDateTime inputDateTime, LocalDateTime outputDateTime,
            SearchResp search, Content content) {
        List<VectorSearchRankDto> rankList = search.getSearchResults().stream()
                .flatMap(List::stream)
                .map(result -> {
                    Map<String, Object> entity = result.getEntity();
                    return VectorSearchRankDto.builder()
                            .answer((String) entity.get("answer"))
                            .title((String) entity.get("title"))
                            .score(result.getScore())
                            .id((Long) result.getId())
                            .build();
                })
                .collect(Collectors.toList());
        String firstAnswer = rankList.isEmpty() ? "" : rankList.get(0).getAnswer();
        return ChatResponseDto.builder()
                .sessionId(sessionId)
                .projectKey(projectKey)
                .input(input)
                .vectorOutput(firstAnswer)
                .output(output)
                .inputDateTime(inputDateTime)
                .outputDateTime(outputDateTime)
                .rank(rankList)
                .content(content)
                .build();
    }
}
