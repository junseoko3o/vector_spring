package com.milvus.vector_spring.invite;

import com.milvus.vector_spring.common.annotation.RequireToken;
import com.milvus.vector_spring.invite.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/invite")
@RequiredArgsConstructor
public class InviteController {
    private final InviteService inviteService;

    @PostMapping()
    @RequireToken
    public ResponseEntity<InviteResponseDto> inviteUser(@Validated @RequestBody InviteUserRequestDto inviteUserRequestDto) {
        Invite invite = inviteService.inviteUser(inviteUserRequestDto);
        return ResponseEntity.ok(InviteResponseDto.inviteResponseDto(invite));
    }

    @PostMapping("/list")
    @RequireToken
    public List<CombinedProjectListResponseDto> invitedProjectAndCreateProjectList(@Validated @RequestBody InvitedProjectMyProjectRequestDto invitedProjectMyProjectRequestDto) {
        return inviteService.invitedProjectAndCreateProjectList(invitedProjectMyProjectRequestDto);
    }

    @GetMapping("/list")
    @RequireToken
    public InvitedProjectUserResponseDto invitedProjectUserList(@RequestParam("key") String projectKey) {
        List<Invite> invitedList = inviteService.findByInvitedProjectUserList(projectKey);
        return invitedList.stream()
                .collect(Collectors.groupingBy(invite -> invite.getProject().getKey()))
                .entrySet().stream()
                .map(entry -> InvitedProjectUserResponseDto.builder()
                        .projectKey(entry.getKey())
                        .createdUserId(entry.getValue().get(0).getCreatedBy().getId())
                        .receivedEmail(entry.getValue().stream()
                                .map(Invite::getReceivedEmail)
                                .toList())
                        .build())
                .findFirst()
                .orElse(null);
    }

    @PostMapping("/change/master")
    @RequireToken
    public String changeMasterUser(@Validated @RequestBody UpdateMasterUserRequestDto updateMasterUserRequestDto) {
        inviteService.updateMasterUser(updateMasterUserRequestDto);
        return "Finish change to " + updateMasterUserRequestDto.getChangeMasterUser() + "!!";
    }

    @DeleteMapping("/banish")
    @RequireToken
    public String banishUser(@Validated @RequestBody BanishUserRequestDto banishUserRequestDto) {
        return inviteService.banishUserFromProject(banishUserRequestDto);
    }
}
