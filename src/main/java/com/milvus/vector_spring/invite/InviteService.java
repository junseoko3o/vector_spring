package com.milvus.vector_spring.invite;

import com.milvus.vector_spring.common.apipayload.status.ErrorStatus;
import com.milvus.vector_spring.common.exception.CustomException;
import com.milvus.vector_spring.invite.dto.*;
import com.milvus.vector_spring.project.Project;
import com.milvus.vector_spring.project.ProjectService;
import com.milvus.vector_spring.project.dto.ProjectResponseDto;
import com.milvus.vector_spring.user.User;
import com.milvus.vector_spring.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InviteService {
    private final UserService userService;
    private final ProjectService projectService;
    private final InviteRepository inviteRepository;

    private Invite findInviteIndexForBanish(Project project, String basnishEmail) {
        return inviteRepository.findByProjectAndReceivedEmail(project, basnishEmail)
                .orElseThrow(() -> new CustomException(ErrorStatus.NOT_INVITED_USER));
    }

    private List<Invite> findByReceivedEmail(String receivedEmail) {
        return inviteRepository.findByReceivedEmail(receivedEmail)
                .orElseThrow(() -> new CustomException(ErrorStatus.NOT_INVITED_USER));
    }

    public List<Invite> findByInvitedProjectUserList(String projectKey) {
        Project project = projectService.findOneProjectByKey(projectKey);
        return inviteRepository.findByProject(project)
                .orElseThrow(() -> new CustomException(ErrorStatus.NOT_FOUND_PROJECT));
    }

    private List<Invite> findByCreatedByAndProject(User user, Project project) {
        return inviteRepository.findByCreatedByAndProject(user, project)
                .orElseThrow(() -> new CustomException(ErrorStatus.NOT_INVITED_USER));
    }

    public List<CombinedProjectListResponseDto> invitedProjectAndCreateProjectList(InvitedProjectMyProjectRequestDto invitedProjectMyProjectRequestDto) {
        User user = userService.findOneUser(invitedProjectMyProjectRequestDto.getUserId());
        List<ProjectResponseDto> myProjectList = userService.fineOneUserWithProjects(user.getId()).getProjects();
        List<Project> receivedProjectList = findByReceivedEmail(user.getEmail()).stream()
                .map(Invite::getProject)
                .toList();

        List<CombinedProjectListResponseDto> combinedProject = new ArrayList<>();
        myProjectList.forEach(myProject -> {
            Project project = projectService.findOneProjectByKey(myProject.getKey());
            combinedProject.add(CombinedProjectListResponseDto.CombinedProjectListResponseDto(project, true));
        });

        receivedProjectList.forEach(receivedProject -> {
            combinedProject.add(CombinedProjectListResponseDto.CombinedProjectListResponseDto(receivedProject, false));
        });

        return combinedProject;
    }

    public Invite inviteUser(InviteUserRequestDto inviteUserRequestDto) {
        User invitedUser = userService.findOneUser(inviteUserRequestDto.getInviteId());
        User receivedUser = userService.findOneUserByEmail(inviteUserRequestDto.getReceiveEmail());
        Project project = projectService.findOneProjectByKey(inviteUserRequestDto.getProjectKey());

        Invite invite = Invite.builder()
                .receivedEmail(receivedUser.getEmail())
                .createdBy(invitedUser)
                .project(project)
                .build();

        return inviteRepository.save(invite);
    }

    public String banishUserFromProject(BanishUserRequestDto banishUserRequestDto) {
        User banishUserEmail = userService.findOneUserByEmail(banishUserRequestDto.getBanishedEmail());
        Project project = projectService.findOneProjectByKey(banishUserRequestDto.getProjectKey());

        Invite invite = findInviteIndexForBanish(project, banishUserEmail.getEmail());
        inviteRepository.delete(invite);
        return "Banish User!";
    }

    public void updateMasterUser(UpdateMasterUserRequestDto updateMasterUserRequestDto) {
        User beforeMasterUser = userService.findOneUser(updateMasterUserRequestDto.getCreatedUserId());
        User afterMasterUser = userService.findOneUserByEmail(updateMasterUserRequestDto.getChangeMasterUser());

        Project project = projectService.findOneProjectByKey(updateMasterUserRequestDto.getProjectKey());
        projectService.updateProjectMaster(project, afterMasterUser);

        List<Invite> invited = findByCreatedByAndProject(beforeMasterUser, project);
        Invite toDelete = invited.stream()
                .filter(invite -> afterMasterUser.getEmail().equals(invite.getReceivedEmail()))
                .toList().get(0);
        inviteRepository.delete(toDelete);
        invited.remove(toDelete);
        for (Invite invite : invited) {
            invite.updateCreatedBy(afterMasterUser);
        }
        inviteRepository.saveAll(invited);
    }
}
