package com.milvus.vector_spring.common.apipayload.status;

import com.milvus.vector_spring.common.apipayload.BaseCode;
import com.milvus.vector_spring.common.apipayload.dto.ResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
@AllArgsConstructor
public enum SuccessStatus implements BaseCode {

    _OK(HttpStatus.OK, "200", "Ok"),
    _CREATED(HttpStatus.CREATED, "201", "Created"),;

    private HttpStatus httpStatus;
    private String statusCode;
    private String message;

    @Override
    public ResponseDto getReasonHttpStatus() {
        return ResponseDto.builder()
                .statusCode(statusCode)
                .message(message)
                .httpStatus(httpStatus)
                .build();
    }
}