package com.milvus.vector_spring.statistics;

import com.milvus.vector_spring.statistics.dto.MongoChatResponse;
import com.milvus.vector_spring.statistics.dto.MongoFindDataDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class StatisticsController {
    private final StatisticsService statisticsService;

    @PostMapping("/{projectKey}")
    public List<MongoChatResponse> getByProjectKey(@PathVariable("projectKey") String projectKey) {
        return statisticsService.findByProjectKey(projectKey);
    }

    @PostMapping()
    public List<MongoChatResponse> getByProjectKey(@RequestBody MongoFindDataDto mongoFindDataDto) {
        return statisticsService.findByProjectKeyAndSessionId(mongoFindDataDto);
    }
}
