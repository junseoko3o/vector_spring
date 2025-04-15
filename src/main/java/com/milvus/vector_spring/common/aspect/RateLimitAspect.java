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

@Aspect
@Component
public class RateLimitAspect {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private HttpServletRequest request;

    private static final int MAX_REQUESTS = 20;
    private static final Duration DURATION = Duration.ofHours(24);

    @Around("@annotation(rateLimit)")
    public Object around(ProceedingJoinPoint joinPoint, RateLimit rateLimit) throws Throwable {
        String ip = getClientIp();
        String key = "rate_limit:ip:" + ip;

        String currentCountStr = redisTemplate.opsForValue().get(key);
        int currentCount = currentCountStr == null ? 0 : Integer.parseInt(currentCountStr);

        if (currentCount >= MAX_REQUESTS) {
            throw new RuntimeException("횟수 초과");
        }

        Long newCount = redisTemplate.opsForValue().increment(key, 1);
        if (newCount != null && newCount == 1L) {
            redisTemplate.expire(key, DURATION);
        }

        return joinPoint.proceed();
    }

    private String getClientIp() {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}

