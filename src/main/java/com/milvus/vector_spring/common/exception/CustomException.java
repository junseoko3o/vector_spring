package com.milvus.vector_spring.common.exception;

import com.milvus.vector_spring.common.apipayload.BaseCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomException extends RuntimeException {
    private final BaseCode baseCode;
}
