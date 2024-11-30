package com.milvus.vector_spring.common.exception;

import com.milvus.vector_spring.common.apipayload.ApiResponse;
import com.milvus.vector_spring.common.apipayload.BaseCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleRuntimeException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<String>> handleCustomException(CustomException e) {
        BaseCode errorCode = e.getBaseCode();
        return handleExceptionInternal(errorCode);
    }

    private ResponseEntity<ApiResponse<String>> handleExceptionInternal(BaseCode errorCode) {
        return ResponseEntity.status(errorCode.getReasonHttpStatus().getHttpStatus())
                .body(ApiResponse.fail(errorCode));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<String>> handleBindException(MethodArgumentNotValidException ex) {
        String errorCodes = ex.getBindingResult().getAllErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.joining(","));

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(false, "400", errorCodes, null));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleAllExceptions(Exception ex) {
        String errorMessage = ex.getMessage() != null ? ex.getMessage() : "An unexpected server error occurred";

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(
                        false,
                        "500",
                        errorMessage,
                        null
                ));
    }
}