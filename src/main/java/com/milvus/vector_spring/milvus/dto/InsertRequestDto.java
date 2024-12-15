package com.milvus.vector_spring.milvus.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class InsertRequestDto {
    @NotNull
    private long id;

    @NotNull
    private List<Float> vector;

    @NotNull
    private String title;

    @NotNull
    private String answer;


    @Builder
    public InsertRequestDto(long id, List<Float> vector, String title, String answer) {
        this.id = id;
        this.vector = vector;
        this.title = title;
        this.answer = answer;
    }
}
