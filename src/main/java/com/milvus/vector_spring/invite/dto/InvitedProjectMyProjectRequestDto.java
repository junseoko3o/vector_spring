package com.milvus.vector_spring.invite.dto;

import lombok.Data;

@Data
public class InvitedProjectMyProjectRequestDto {
    private String projectKey;
    private Long userId;
}
