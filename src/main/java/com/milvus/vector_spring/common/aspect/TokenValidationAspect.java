package com.milvus.vector_spring.common.aspect;

import com.milvus.vector_spring.common.annotation.RequireToken;
import com.milvus.vector_spring.common.apipayload.status.ErrorStatus;
import com.milvus.vector_spring.common.exception.CustomException;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class TokenValidationAspect {

    @Around("@annotation(requireToken)")
    public Object validateToken(ProceedingJoinPoint joinPoint, RequireToken requireToken) {
        try {
            HttpServletRequest request = getCurrentHttpRequest();
            String token = extractToken(request);

            if (!isValidToken(token)) {
                throw new CustomException(ErrorStatus.INVALID_TOKEN);
            }

            Object[] args = joinPoint.getArgs();
            if (args.length > 0 && args[0] instanceof String) {
                args[0] = token;
            }

            return joinPoint.proceed(args);
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
        // TODO: 실제 토큰 검증 로직 구현
        return token != null && !token.isEmpty();
    }

    //          Cookie[] cookies;
//        String tokens = getAccessTokenFromCookies(request.getCookies());
//        if (cookies != null) {
//            for (Cookie cookie : cookies) {
//                if (COOKIE_NAME.equals(cookie.getName())) {
//                    return cookie.getValue();
//                }
//            }
//        }
}
