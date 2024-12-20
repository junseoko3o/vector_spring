package com.milvus.vector_spring.invite.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class InvitedProjectUserResponseDto {
    private String projectKey;
    private Long createdUserId;
    private List<String> receivedEmail;
}
