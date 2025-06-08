package com.milvus.vector_spring.config.mongo.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Document(collection = "chat_response")
public class ChatResponseDocument {

    @Id
    private String id;

    private String sessionId;
    private String projectKey;
    private String input;
    private String output;
    private String vectorOutput;
    private Map<String, Object> content;

    private LocalDateTime inputDateTime;
    private LocalDateTime outputDateTime;

    private List<Rank> rank;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Rank {
        private String answer;
        private String title;
        private double score;
        private int id;
    }
}

