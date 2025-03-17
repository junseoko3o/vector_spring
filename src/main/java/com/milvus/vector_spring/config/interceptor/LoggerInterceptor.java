package com.milvus.vector_spring.config.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


@Component
public class LoggerInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(LoggerInterceptor.class);

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String method = request.getMethod();
        String url = request.getRequestURI();
        int statusCode = response.getStatus();

        if (statusCode < 400) {
            logger.info("{} {} {}", method, statusCode, url);
        } else {
            logger.error("{} {} {} - Status: {}", method, statusCode, url, response.getStatus());
        }
    }
}
