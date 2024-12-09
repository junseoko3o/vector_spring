package com.milvus.vector_spring.milvus.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class InsertRequestDto {
    private long id;
    private List<Double> vector;
    private String title;
    private String answer;


    @Builder
    public InsertRequestDto(long id, List<Double> vector, String title, String answer) {
        this.id = id;
        this.vector = vector;
        this.title = title;
        this.answer = answer;
    }
}
