package com.milvus.vector_spring.invite;

import com.milvus.vector_spring.common.apipayload.status.ErrorStatus;
import com.milvus.vector_spring.common.exception.CustomException;
import com.milvus.vector_spring.invite.dto.*;
import com.milvus.vector_spring.project.Project;
import com.milvus.vector_spring.project.ProjectCommandService;
import com.milvus.vector_spring.project.ProjectQueryService;
import com.milvus.vector_spring.project.dto.ProjectResponseDto;
import com.milvus.vector_spring.user.User;
import com.milvus.vector_spring.user.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class InviteServiceImpl implements InviteService {

    private final UserQueryService userQueryService;
    private final ProjectQueryService projectQueryService;
    private final ProjectCommandService projectCommandService;
    private final InviteRepository inviteRepository;

    private Invite findInviteIndexForBanish(Project project, String banishEmail) {
        return inviteRepository.findByProjectAndReceivedEmail(project, banishEmail)
                .orElseThrow(() -> new CustomException(ErrorStatus.NOT_INVITED_USER));
    }

    private List<Invite> findByReceivedEmail(String receivedEmail) {
        return inviteRepository.findByReceivedEmail(receivedEmail)
                .orElseThrow(() -> new CustomException(ErrorStatus.NOT_INVITED_USER));
    }

    @Override
    public List<Invite> findByInvitedProjectUserList(String projectKey) {
        Project project = projectQueryService.findOneProjectByKey(projectKey);
        return inviteRepository.findByProject(project)
                .orElseThrow(() -> new CustomException(ErrorStatus.NOT_FOUND_PROJECT));
    }

    private List<Invite> findByCreatedByAndProject(User user, Project project) {
        return inviteRepository.findByCreatedByAndProject(user, project)
                .orElseThrow(() -> new CustomException(ErrorStatus.NOT_INVITED_USER));
    }

    @Override
    public List<CombinedProjectListResponseDto> invitedProjectAndCreateProjectList(InvitedProjectMyProjectRequestDto dto) {
        User user = userQueryService.findOneUser(dto.getUserId());
        List<ProjectResponseDto> myProjectList = userQueryService.fineOneUserWithProjects(user.getId()).getProjects();
        List<Project> receivedProjectList = findByReceivedEmail(user.getEmail()).stream()
                .map(Invite::getProject)
                .toList();

        List<CombinedProjectListResponseDto> combinedProject = new ArrayList<>();
        myProjectList.forEach(myProject -> {
            Project project = projectQueryService.findOneProjectByKey(myProject.getKey());
            combinedProject.add(CombinedProjectListResponseDto.CombinedProjectListResponseDto(project, true));
        });

        receivedProjectList.forEach(receivedProject -> {
            combinedProject.add(CombinedProjectListResponseDto.CombinedProjectListResponseDto(receivedProject, false));
        });

        return combinedProject;
    }

    @Override
    public Invite inviteUser(InviteUserRequestDto dto) {
        User invitedUser = userQueryService.findOneUser(dto.getInviteId());
        User receivedUser = userQueryService.findOneUserByEmail(dto.getReceiveEmail());
        Project project = projectQueryService.findOneProjectByKey(dto.getProjectKey());
        if (!Objects.equals(invitedUser.getId(), project.getCreatedBy().getId())) {
            throw new CustomException(ErrorStatus.NOT_PROJECT_MASTER_USER);
        }
        Invite invite = Invite.builder()
                .receivedEmail(receivedUser.getEmail())
                .createdBy(invitedUser)
                .project(project)
                .build();

        return inviteRepository.save(invite);
    }

    @Override
    public String banishUserFromProject(BanishUserRequestDto dto) {
        User masterUser = userQueryService.findOneUserByEmail(dto.getMasterUserEmail());
        User banishUser = userQueryService.findOneUserByEmail(dto.getBanishedEmail());
        Project project = projectQueryService.findOneProjectByKey(dto.getProjectKey());
        if (!Objects.equals(masterUser.getId(), project.getCreatedBy().getId())) {
            throw new CustomException(ErrorStatus.NOT_PROJECT_MASTER_USER);
        }
        Invite invite = findInviteIndexForBanish(project, banishUser.getEmail());
        inviteRepository.delete(invite);
        return "Banish User!";
    }

    @Override
    public void updateMasterUser(UpdateMasterUserRequestDto dto) {
        User beforeMaster = userQueryService.findOneUser(dto.getCreatedUserId());
        User afterMaster = userQueryService.findOneUserByEmail(dto.getChangeMasterUser());

        Project project = projectQueryService.findOneProjectByKey(dto.getProjectKey());
        projectCommandService.updateProjectMaster(project, afterMaster);

        List<Invite> invites = findByCreatedByAndProject(beforeMaster, project);
        Invite toDelete = invites.stream()
                .filter(invite -> afterMaster.getEmail().equals(invite.getReceivedEmail()))
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorStatus.NOT_INVITED_USER));

        inviteRepository.delete(toDelete);
        invites.remove(toDelete);

        for (Invite invite : invites) {
            invite.updateCreatedBy(afterMaster);
        }
        inviteRepository.saveAll(invites);
    }
}
