package com.milvus.vector_spring.invite.dto;

import com.milvus.vector_spring.invite.Invite;
import com.milvus.vector_spring.project.Project;
import com.milvus.vector_spring.project.dto.ProjectResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class InviteResponseDto {
    private final Long id;
    private final String receivedEmail;
    private final String projectKey;
    private final Long invitedId;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static InviteResponseDto inviteResponseDto(Invite invite) {
        return InviteResponseDto.builder()
                .id(invite.getId())
                .receivedEmail(invite.getReceivedEmail())
                .invitedId(invite.getCreatedBy().getId())
                .projectKey(invite.getProject().getKey())
                .createdAt(invite.getCreatedAt())
                .updatedAt(invite.getUpdatedAt())
                .build();
    }
}
