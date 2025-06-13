package com.milvus.vector_spring.config.mongo.document;

import com.milvus.vector_spring.chat.dto.VectorSearchRankDto;
import com.milvus.vector_spring.content.Content;
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
    private String vectorOutput;
    private Content content;
    private LocalDateTime inputDateTime;
    private LocalDateTime outputDateTime;
    private List<VectorSearchRankDto> rank;

    public ChatResponseDocument(String sessionId, String input, String output,
                                String vectorOutput, LocalDateTime inputDateTime,
                                LocalDateTime outputDateTime, Content content,
                                List<VectorSearchRankDto> rank) {
        this.sessionId = sessionId;
        this.input = input;
        this.output = output;
        this.vectorOutput = vectorOutput;
        this.inputDateTime = inputDateTime;
        this.outputDateTime = outputDateTime;
        this.content = content;
        this.rank = rank;
    }
}

