package com.milvus.vector_spring.common.apipayload.status;

import com.milvus.vector_spring.common.apipayload.BaseCode;
import com.milvus.vector_spring.common.apipayload.dto.ResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseCode {
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Server Error. Send Mail"),
    DUPLICATE_USER_EMAIL(HttpStatus.BAD_REQUEST, "Duplicate User Email."),
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "User Not Found."),
    NOT_PASSWORD_MATCHES(HttpStatus.BAD_REQUEST, "Different Password."),
    NOT_FOUND_PROJECT(HttpStatus.NOT_FOUND, "Not Found Project."),
    DATABASE_ERROR(HttpStatus.BAD_REQUEST, "Database Error."),
    SQL_ERROR(HttpStatus.BAD_REQUEST, "SQL Error."),
    MILVUS_DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Milvus Error."),
    OPEN_AI_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Open AI Error."),
    NOT_FOUND_CONTENT(HttpStatus.NOT_FOUND, "Not Found Content."),
    DECRYPTION_ERROR(HttpStatus.BAD_REQUEST, "Decryption Error."),
    ENCRYPTION_ERROR(HttpStatus.BAD_REQUEST, "Encryption Error."),
    OPEN_AI_KEY_ERROR(HttpStatus.UNAUTHORIZED, "Invalid OpenAi Key."),
    NOT_INVITED_USER(HttpStatus.BAD_REQUEST, "Not Invited."),
    COOKIE_ENCODING_ERROR(HttpStatus.UNAUTHORIZED, "Cookie Encoding Error."),
    EMPTY_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "Not Found Refresh Token."),
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "Invalid Access Token."),
    REQUIRE_OPEN_AI_KEY(HttpStatus.BAD_REQUEST, "Not Found Open AI Key."),
    REQUIRE_OPEN_AI_INFO(HttpStatus.BAD_REQUEST, "Open AI Info Error."),
    TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "Token is required"),
    INVALID_TOKEN_FORMAT(HttpStatus.UNAUTHORIZED, "Invalid token format"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "Invalid token"),
    EMAIL_NOT_REGISTERED(HttpStatus.BAD_REQUEST, "Email not registered."),
    EXPIRED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "Expired refresh token"),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "Invalid password."),
    NOT_PROJECT_MASTER_USER(HttpStatus.UNAUTHORIZED, "Not Project Master User."),
    EMBEDDING_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create embedding."),
    UNKNOWING_MODEL(HttpStatus.BAD_REQUEST, "Unknown GPT Model."),
    MILVUS_SCHEMA_CREATE_ERROR(HttpStatus.BAD_REQUEST, "Schema Create Error."),
    MILVUS_INDEX_CREATE_ERROR(HttpStatus.BAD_REQUEST, "Index Create Error."),
    MILVUS_UPSERT_ERROR(HttpStatus.BAD_REQUEST, "Upsert Error."),
    MILVUS_DELETE_ERROR(HttpStatus.BAD_REQUEST, "Delete Error."),
    MILVUS_COLLECTION_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "Collection Already Exists."),
    MILVUS_VECTOR_SEARCH_ERROR(HttpStatus.BAD_REQUEST, "Vector Search Error."),
    DATA_PARSE_ERROR(HttpStatus.BAD_REQUEST, "Data Parse Error."),
    OPENAI_EMBEDDING_ERROR(HttpStatus.BAD_REQUEST, "OpenAi Embedding Error.");

    private final HttpStatus httpStatus;
    private final String message;

    public String getStatusCode() {
        return String.valueOf(httpStatus.value());
    }

    @Override
    public ResponseDto getReasonHttpStatus() {
        return ResponseDto.builder()
                .statusCode(getStatusCode())
                .message(message)
                .httpStatus(httpStatus)
                .build();
    }
}
