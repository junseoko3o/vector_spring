package com.milvus.vector_spring.statistics;

import com.milvus.vector_spring.statistics.dto.MongoChatResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
