package com.milvus.vector_spring.common;

import com.milvus.vector_spring.common.service.RedisService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RedisServiceTest {

    private RedisService redisService;

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private RedisConnectionFactory redisConnectionFactory;

    @Mock
    private RedisConnection redisConnection;

    @Mock
    private ValueOperations<String, Object> valueOperations;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(redisTemplate.getConnectionFactory()).thenReturn(redisConnectionFactory);
        when(redisConnectionFactory.getConnection()).thenReturn(redisConnection);
        redisService = new RedisService(redisTemplate);
    }

    @Test
    @DisplayName("레디스 연결 체크")
    void test_redis_connection_check() {
        when(redisConnection.ping()).thenReturn("PONG");

        boolean result = redisService.check();

        assertTrue(result);
    }

    @Test
    @DisplayName("레디스 연결 실패")
    void test_redis_connection_fail() {
        when(redisConnection.ping()).thenReturn(null);

        boolean result = redisService.check();

        assertFalse(result);
    }

    @Test
    @DisplayName("레디스 연결 실패")
    void test_redis_connection_fail_with_exception() {
        when(redisConnectionFactory.getConnection()).thenThrow(new RuntimeException("Connection error"));

        boolean result = redisService.check();

        assertFalse(result);
    }

    @Test
    @DisplayName("레디스 데이터 삽입")
    void redis_set() {
        String key = "testKey";
        String value = "testValue";
        int ttl = 60;
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        redisService.setRedis(key, value, ttl);

        verify(valueOperations, times(1)).set(key, value, ttl, TimeUnit.SECONDS);
    }

    @Test
    @DisplayName("레디스 데이터 찾기")
    void redis_get() {
        String key = "testKey";
        String expectedValue = "testValue";
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(key)).thenReturn(expectedValue);

        String result = redisService.getRedis(key);

        assertNotNull(result);
        verify(valueOperations, times(1)).get(key);
    }

    @Test
    @DisplayName("레디스 데이터 삭제")
    void redis_delete() {
        String key = "testKey";
        String expectedValue = "testValue";
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.getAndDelete(key)).thenReturn(expectedValue);

        String result = redisService.deleteRedis(key);

        assertEquals("deleted", result);
        verify(valueOperations, times(1)).getAndDelete(key);
    }
}
