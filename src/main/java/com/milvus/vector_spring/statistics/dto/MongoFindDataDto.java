package com.milvus.vector_spring.statistics.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MongoFindDataDto {
    private String projectKey;
    private String sessionId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
