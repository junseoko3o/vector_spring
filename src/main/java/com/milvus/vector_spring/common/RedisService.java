package com.milvus.vector_spring.common;

import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class RedisService {
    private final RedisTemplate<String, Object> redisTemplate;

    public RedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean check() {
        try (RedisConnection connection = Objects.requireNonNull(redisTemplate.getConnectionFactory()).getConnection()) {
            String pingResponse = connection.ping();
            return "PONG".equalsIgnoreCase(pingResponse);
        } catch (Exception e) {
            return false;
        }
    }

    public void setRedis(String key, String value, int ttl) {
        ValueOperations<String, Object> data = redisTemplate.opsForValue();
        data.set(key, value, ttl);
    }

    public ValueOperations<String, Object> getRedis(String key) {
        ValueOperations<String, Object> data = redisTemplate.opsForValue();
        data.get(key);
        return data;
    }
}
