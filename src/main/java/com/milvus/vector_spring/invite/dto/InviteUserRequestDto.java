package com.milvus.vector_spring.invite.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InviteUserRequestDto {

    @NotNull
    private Long inviteId;

    @NotNull
    @NotBlank
    @Email
    private String receiveEmail;

    @NotNull
    @NotBlank
    private String projectKey;
}
