package com.milvus.vector_spring.common.aspect;

import com.milvus.vector_spring.common.annotation.NoAuthRequired;
import com.milvus.vector_spring.common.apipayload.status.ErrorStatus;
import com.milvus.vector_spring.common.exception.CustomException;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;

@Aspect
@Component
public class TokenValidationAspect {

    @Around("execution(* com.milvus.vector_spring..*Controller.*(..))")
    public Object validateToken(ProceedingJoinPoint joinPoint) {
        try {
            Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();

            if (method.isAnnotationPresent(NoAuthRequired.class)) {
                return joinPoint.proceed();
            }

            HttpServletRequest request = getCurrentHttpRequest();
            String token = extractToken(request);

            if (!isValidToken(token)) {
                throw new CustomException(ErrorStatus.INVALID_TOKEN);
            }

            return joinPoint.proceed();

        } catch (CustomException e) {
            throw e;
        } catch (Throwable e) {
            throw new CustomException(ErrorStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private HttpServletRequest getCurrentHttpRequest() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) {
            throw new CustomException(ErrorStatus.INTERNAL_SERVER_ERROR);
        }
        return attrs.getRequest();
    }

    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");

        if (header == null || header.isEmpty()) {
            throw new CustomException(ErrorStatus.TOKEN_NOT_FOUND);
        }

        if (!header.startsWith("Bearer ")) {
            throw new CustomException(ErrorStatus.INVALID_TOKEN_FORMAT);
        }

        return header.substring(7);
    }

    private boolean isValidToken(String token) {
        return token != null && !token.isEmpty();
    }
}
