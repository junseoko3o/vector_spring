package com.milvus.vector_spring.content.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
@JsonNaming(value = PropertyNamingStrategy.class)
public class ContentCreateRequestDto {

    private String title;
    private String answer;

    @NotNull
    private Long userId;
}
