package com.milvus.vector_spring.milvus.dto;

import java.util.List;

public class InsertRequestDto {
    private String id;
    private List<Float> vector;
    private String title;
    private String answer;
}
