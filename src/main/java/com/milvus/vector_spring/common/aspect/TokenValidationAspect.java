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
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String token = request.getHeader("Authorization");

            if (token == null || token.isEmpty()) {
                throw new CustomException(ErrorStatus._TOKEN_NOT_FOUND);
            }

            if (!token.startsWith("Bearer ")) {
                throw new CustomException(ErrorStatus._INVALID_TOKEN_FORMAT);
            }

            token = token.substring(7);

            if (!isValidToken(token)) {
                throw new CustomException(ErrorStatus._INVALID_TOKEN);
            }

            Object[] args = joinPoint.getArgs();

            if (args.length > 0 && args[0] instanceof String) {
                args[0] = token;
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

            return joinPoint.proceed(args);
        } catch (CustomException e) {
            throw e;
        } catch (Throwable e) {
            throw new CustomException(ErrorStatus._INTERNAL_SERVER_ERROR);
        }
    }

    private boolean isValidToken(String token) {
        // TODO: 실제 토큰 검증 로직 구현
        return token != null && !token.isEmpty();
    }
}
