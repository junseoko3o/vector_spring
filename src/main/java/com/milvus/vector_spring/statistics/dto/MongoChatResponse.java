package com.milvus.vector_spring.statistics.dto;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;
import java.util.Map;
@Data
@Document(collection = "chat_response")
public class MongoChatResponse {
    @Id
    private String id;

    private String sessionId;
    private String projectKey;
    private String input;
    private String output;
    private String vectorOutput;
    private Map<String, Object> content; // flexible object
    private Date inputDateTime;
    private Date outputDateTime;
    private List<Rank> rank;

    @Data
    public static class Rank {
        private String answer;
        private String title;
        private double score;
        private int id;
    }
}
