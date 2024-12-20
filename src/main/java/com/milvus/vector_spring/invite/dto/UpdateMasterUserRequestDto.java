package com.milvus.vector_spring.invite.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
public class UpdateMasterUserRequestDto {
    @NotNull
    private String projectKey;

    @NotNull
    private Long createdUserId;

    @NotNull
    private String changeMasterUser;
}
