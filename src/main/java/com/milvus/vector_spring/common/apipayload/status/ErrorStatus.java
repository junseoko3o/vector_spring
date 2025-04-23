package com.milvus.vector_spring.common.apipayload.status;

import com.milvus.vector_spring.common.apipayload.BaseCode;
import com.milvus.vector_spring.common.apipayload.dto.ResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseCode {
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "500", "Server Error."),
    _DUPLICATE_USER_EMAIL(HttpStatus.BAD_REQUEST, "400", "Duplicate User Email."),
    _NOT_FOUND_USER(HttpStatus.NOT_FOUND, "400", "User Not Found."),
    _NOT_PASSWORD_MATCHES(HttpStatus.BAD_REQUEST, "400", "Different Password."),
    _NOT_FOUND_PROJECT(HttpStatus.NOT_FOUND, "400", "Not Found Project."),
    _DATABASE_ERROR(HttpStatus.BAD_REQUEST, "400", "Database Error."),
    _SQL_ERROR(HttpStatus.BAD_REQUEST, "400", "SQL Error."),
    _MILVUS_DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "500", "Milvus Error."),
    _OPEN_AI_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "500", "Open AI Error."),
    _NOT_FOUND_CONTENT(HttpStatus.NOT_FOUND, "400", "Not Found Content."),
    _DECRYPTION_ERROR(HttpStatus.BAD_REQUEST, "400", "Decryption Error."),
    _ENCRYPTION_ERROR(HttpStatus.BAD_REQUEST, "400", "Encryption Error."),
    _OPEN_AI_KEY_ERROR(HttpStatus.UNAUTHORIZED, "403", "Invalid OpenAi Key."),
    _NOT_INVITED_USER(HttpStatus.BAD_REQUEST, "400", "Not Invited."),
    _COOKIE_ENCODING_ERROR(HttpStatus.UNAUTHORIZED, "401", "Cookie Encoding Error."),
    _EMPTY_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "401", "Not Found Refresh Token."),
    _INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "401", "Invalid Access Token."),
    _REQUIRE_OPEN_AI_KEY(HttpStatus.BAD_REQUEST, "400", "Not Found Open AI Key."),
    _REQUIRE_OPEN_AI_INFO(HttpStatus.BAD_REQUEST, "400", "Open AI Info Error."),
    _TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED,"401", "Token is required"),
    _INVALID_TOKEN_FORMAT(HttpStatus.UNAUTHORIZED, "401", "Invalid token format"),
    _INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "401", "Invalid token"),
    _EXPIRED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "401", "Expired refresh token"),
    ;


    private final HttpStatus httpStatus;
    private final String statusCode;
    private final String message;

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
                .build();
    }
}