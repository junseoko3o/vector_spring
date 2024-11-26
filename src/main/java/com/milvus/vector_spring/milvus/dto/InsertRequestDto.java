package com.milvus.vector_spring.milvus.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class InsertRequestDto {
    private String id;
    private List<Float> vector;
    private String title;
    private String answer;
}
