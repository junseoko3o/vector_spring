package com.milvus.vector_spring.invite.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BanishUserRequestDto {
    private String masterUserEmail;
    private String banishedEmail;
    private String projectKey;
}
