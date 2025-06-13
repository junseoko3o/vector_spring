package com.milvus.vector_spring.config.mongo.document;

import com.milvus.vector_spring.chat.dto.VectorSearchRankDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Document(collection = "chat_response")
public class ChatResponseDocument {

    @Id
    private String id;

    private String sessionId;
    private String projectKey;
    private String input;
    private String output;
    private SimpleContentDto content;
    private LocalDateTime inputDateTime;
    private LocalDateTime outputDateTime;
    private List<VectorSearchRankDto> rank;

    @Data
    @AllArgsConstructor
    public static class SimpleContentDto {
        private Long id;
        private String key;
        private String title;
        private String answer;
    }

    public ChatResponseDocument(String sessionId, String input, String output,
                                LocalDateTime inputDateTime,
                                LocalDateTime outputDateTime, SimpleContentDto content,
                                List<VectorSearchRankDto> rank) {
        this.sessionId = sessionId;
        this.input = input;
        this.output = output;
        this.inputDateTime = inputDateTime;
        this.outputDateTime = outputDateTime;
        this.content = content;
        this.rank = rank;
    }
}

