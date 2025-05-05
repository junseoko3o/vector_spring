package com.milvus.vector_spring.statistics;

import com.milvus.vector_spring.statistics.dto.MongoChatResponse;
import com.milvus.vector_spring.statistics.dto.MongoFindDataDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class StatisticsService {
    private final MongoTemplate mongoTemplate;

    private static final ZoneId KST_ZONE_ID = ZoneId.of("Asia/Seoul");
    private static final ZoneId UTC_ZONE_ID = ZoneId.of("UTC");

    public List<MongoChatResponse> findByProjectKey(String projectKey) {
        Query query = new Query(Criteria.where("projectKey").is(projectKey));
        return mongoTemplate.find(query, MongoChatResponse.class);
    }

    public List<MongoChatResponse> findByProjectKeyAndSessionId(MongoFindDataDto mongoFindDataDto) {
        Date startKst = convertUtcToKst(mongoFindDataDto.getStartDate());
        Date endKst = convertUtcToKst(mongoFindDataDto.getEndDate());

        Query query = new Query(
                new Criteria().andOperator(
                        Criteria.where("projectKey").is(mongoFindDataDto.getProjectKey()),
                        Criteria.where("sessionId").is(mongoFindDataDto.getSessionId()),
                        Criteria.where("inputDateTime").gte(startKst).lte(endKst)
                )
        );

        List<MongoChatResponse> results = mongoTemplate.find(query, MongoChatResponse.class);

        return results.stream()
                .map(this::convertToKst)
                .collect(Collectors.toList());
    }

    public List<MongoChatResponse> findAllLog() {
        return mongoTemplate.findAll(MongoChatResponse.class);
    }

    private Date convertUtcToKst(Date utcDate) {
        ZonedDateTime utcZonedDateTime = utcDate.toInstant().atZone(UTC_ZONE_ID);
        ZonedDateTime kstZonedDateTime = utcZonedDateTime.withZoneSameInstant(KST_ZONE_ID);
        return Date.from(kstZonedDateTime.toInstant());
    }

    private MongoChatResponse convertToKst(MongoChatResponse response) {
        if (response.getInputDateTime() != null) {
            Date kstDateTime = convertUtcToKst(response.getInputDateTime());
            response.setInputDateTime(kstDateTime);
        }
        return response;
    }
}
