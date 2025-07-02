package com.milvus.vector_spring.invite;

import com.milvus.vector_spring.invite.dto.*;

import java.util.List;

public interface InviteService {

    Invite inviteUser(InviteUserRequestDto dto);

    List<CombinedProjectListResponseDto> invitedProjectAndCreateProjectList(InvitedProjectMyProjectRequestDto dto);

    List<Invite> findByInvitedProjectUserList(String projectKey);

    void updateMasterUser(UpdateMasterUserRequestDto dto);

    String banishUserFromProject(BanishUserRequestDto dto);
}
