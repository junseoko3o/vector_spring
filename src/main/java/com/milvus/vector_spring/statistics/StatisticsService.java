package com.milvus.vector_spring.statistics;

import com.milvus.vector_spring.statistics.dto.MongoChatResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class StatisticsService {
    @Value("${spring.data.mongodb.database}")
    private String databaseName;

    private final MongoTemplate mongoTemplate;

    public List<MongoChatResponse> findByProjectKey(String projectKey) {
        Query query = new Query(Criteria.where("projectKey").is(projectKey));
        return mongoTemplate.find(query, MongoChatResponse.class);
    }
}
