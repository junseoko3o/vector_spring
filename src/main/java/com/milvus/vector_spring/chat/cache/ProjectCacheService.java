package com.milvus.vector_spring.chat.cache;

import com.milvus.vector_spring.project.Project;
import com.milvus.vector_spring.project.ProjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProjectCacheService {

    private final RedisTemplate<String, ProjectCacheDto> redisTemplate;
    private final ProjectService projectService;

    public ProjectCacheService(@Qualifier("projectRedisTemplate")
                               RedisTemplate<String, ProjectCacheDto> redisTemplate,
                               ProjectService projectService) {
        this.redisTemplate = redisTemplate;
        this.projectService = projectService;
    }

    public Project getProject(String key) {
        String cacheKey = "project:" + key;
        ProjectCacheDto cached = null;

        try {
            cached = redisTemplate.opsForValue().get(cacheKey);
            if (cached != null) {
                log.info("[CACHE HIT] Returning project from Redis for key: {}", key);
                return cached.toProject();
            }
        } catch (Exception e) {
            log.warn("[CACHE ERROR] Failed to get from Redis for key: {}", key, e);
        }

        log.info("[CACHE MISS] Fetching project from DB for key: {}", key);
        Project project = projectService.findOneProjectByKey(key);

        try {
            redisTemplate.opsForValue().set(cacheKey, ProjectCacheDto.from(project));
        } catch (Exception e) {
            log.warn("[CACHE SET ERROR] Failed to cache project for key: {}", key, e);
        }

        return project;
    }
}
