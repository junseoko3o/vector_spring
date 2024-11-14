package com.milvus.vector_spring.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    NOT_FOUND_USER("User is not found", HttpStatus.NOT_FOUND),
    INTERNAL_SERVER_ERROR("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String message;
    private final HttpStatus status;

    ErrorCode(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getStatus() {
        return status;
    }
}

