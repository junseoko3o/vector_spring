package com.milvus.vector_spring.common;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

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
    void testCheck_whenPingResponseIsPong_shouldReturnTrue() {
        // Arrange
        when(redisConnection.ping()).thenReturn("PONG");

        // Act
        boolean result = redisService.check();

        // Assert
        assertTrue(result);
    }

    @Test
    @DisplayName("레디스 연결 실패")
    void testCheck_whenPingResponseIsNull_shouldReturnFalse() {
        // Arrange
        when(redisConnection.ping()).thenReturn(null);

        // Act
        boolean result = redisService.check();

        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("레디스 연결 실패")
    void testCheck_whenExceptionOccurs_shouldReturnFalse() {
        // Arrange
        when(redisConnectionFactory.getConnection()).thenThrow(new RuntimeException("Connection error"));

        // Act
        boolean result = redisService.check();

        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("레디스 데이터 삽입")
    void testSetRedis_shouldCallValueOperationsSet() {
        // Arrange
        String key = "testKey";
        String value = "testValue";
        int ttl = 60;
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        // Act
        redisService.setRedis(key, value, ttl);

        // Assert
        verify(valueOperations, times(1)).set(key, value, ttl);
    }

    @Test
    @DisplayName("레디스 데이터 찾기")
    void testGetRedis_shouldReturnValueFromRedis() {
        // Arrange
        String key = "testKey";
        String expectedValue = "testValue";
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(key)).thenReturn(expectedValue);

        // Act
        String result = redisService.getRedis(key);

        // Assert
        assertNotNull(result);
        verify(valueOperations, times(1)).get(key);
    }

    @Test
    @DisplayName("레디스 데이터 삭제")
    void testDeleteRedis_shouldCallGetAndDelete() {
        // Arrange
        String key = "testKey";
        String expectedValue = "testValue";
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.getAndDelete(key)).thenReturn(expectedValue);

        // Act
        String result = redisService.deleteRedis(key);

        // Assert
        assertEquals("deleted", result);
        verify(valueOperations, times(1)).getAndDelete(key);
    }
}
