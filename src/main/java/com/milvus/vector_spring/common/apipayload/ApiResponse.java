package com.milvus.vector_spring.common.apipayload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.milvus.vector_spring.common.apipayload.dto.ResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"statusCode", "message"})
public class ApiResponse implements Serializable {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String statusCode;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;

    public static ApiResponse fail(BaseCode errorCode) {
        ResponseDto reason = errorCode.getReasonHttpStatus();
        return new ApiResponse(reason.getStatusCode(), reason.getMessage());
    }
}