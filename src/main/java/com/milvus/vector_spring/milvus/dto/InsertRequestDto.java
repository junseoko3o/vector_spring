package com.milvus.vector_spring.milvus.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class InsertRequestDto {
    @NotNull
    private final long id;

    @NotNull
    private final List<Float> vector;

    @NotNull
    private final String title;

    @NotNull
    private final String answer;


    @Builder
    public InsertRequestDto(long id, List<Float> vector, String title, String answer) {
        this.id = id;
        this.vector = vector;
        this.title = title;
        this.answer = answer;
    }
}
