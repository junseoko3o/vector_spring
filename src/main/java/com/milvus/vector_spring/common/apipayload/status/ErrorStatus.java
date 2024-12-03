package com.milvus.vector_spring.common.apipayload.status;

import com.milvus.vector_spring.common.apipayload.BaseCode;
import com.milvus.vector_spring.common.apipayload.dto.ResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseCode {
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "500", "요청에 실패하였습니다."),
    _DUPLICATE_USER_EMAIL(HttpStatus.BAD_REQUEST, "400", "중복된 이메일."),
    _NOT_FOUND_USER(HttpStatus.NOT_FOUND, "400", "유저 없음"),
    _NOT_FOUND_PROJECT(HttpStatus.NOT_FOUND, "400", "프로젝트 없음"),
    _DATABASE_ERROR(HttpStatus.BAD_REQUEST, "400", "데이터베이스 오류"),
    _SQL_ERROR(HttpStatus.BAD_REQUEST, "400", "SQL 오류"),
    _MILVUS_DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "500", "Milvus 오류"),
    _OPEN_AI_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "500", "Open AI 에러"),
    _NOT_FOUND_CONTENT(HttpStatus.NOT_FOUND, "400", "컨텐츠 없음"),
    _DECRYPTION_ERROR(HttpStatus.BAD_REQUEST, "400", "복호화 에러"),
    _ENCRYPTION_ERROR(HttpStatus.BAD_REQUEST, "400", "암호화 에러"),
    _OPEN_AI_KEY_ERROR(HttpStatus.UNAUTHORIZED, "403", "OpenAI 키 에러"),
    ;


    private HttpStatus httpStatus;
    private String statusCode;
    private String message;

    public static ErrorStatus getErrorStatus(String findMessage) {
        return Arrays.stream(values())
                .filter(errorStatus -> errorStatus.message.equals(findMessage))
                .findAny()
                .orElse(ErrorStatus._INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseDto getReasonHttpStatus() {
        return ResponseDto.builder()
                .statusCode(statusCode)
                .message(message)
                .httpStatus(httpStatus)
                .success(false)
                .build();
    }
}