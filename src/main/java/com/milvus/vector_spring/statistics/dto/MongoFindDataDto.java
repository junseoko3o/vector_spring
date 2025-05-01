package com.milvus.vector_spring.statistics.dto;

import lombok.Data;

import java.util.Date;

@Data
public class MongoFindDataDto {
    private String projectKey;
    private String sessionId;
    private Date startDate;
    private Date endDate;
}
