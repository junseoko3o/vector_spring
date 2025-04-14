package com.milvus.vector_spring.common.aspect;

import com.milvus.vector_spring.common.annotation.RateLimit;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Aspect
@Component
public class RateLimitAspect {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private HttpServletRequest request;

    private static final int MAX_REQUESTS = 20;
    private static final LocalDateTime TIME_WINDOW = LocalDateTime.now().plusHours(24);

    @Around("@annotation(rateLimit)")
    public Object around(ProceedingJoinPoint joinPoint, RateLimit rateLimit) throws Throwable {
        String ip = request.getRemoteAddr();
        String key = "rate_limit: " + ip;

        String currentCount = redisTemplate.opsForValue().get(key);
        if (currentCount != null && Integer.parseInt(currentCount) >= MAX_REQUESTS) {
            throw new RuntimeException("횟수 초과");
        }
        redisTemplate.opsForValue().increment(key, 1);

        if(currentCount == null) {
            redisTemplate.expire(key, Duration.ofHours(24));
        }

        return joinPoint.proceed();
    }
}
