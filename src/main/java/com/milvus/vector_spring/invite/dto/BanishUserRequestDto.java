package com.milvus.vector_spring.invite.dto;

import lombok.Data;

@Data
public class BanishUserRequestDto {
    private String MasterUserEmail;
    private String banishedEmail;
    private String projectKey;
}
