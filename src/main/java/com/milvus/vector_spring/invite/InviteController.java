package com.milvus.vector_spring.invite;

import com.milvus.vector_spring.invite.dto.CombinedProjectListResponseDto;
import com.milvus.vector_spring.invite.dto.InviteResponseDto;
import com.milvus.vector_spring.invite.dto.InviteUserRequestDto;
import com.milvus.vector_spring.invite.dto.InvitedProjectMyProjectRequestDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/invite")
@RequiredArgsConstructor
public class InviteController {
    private final InviteService inviteService;

    @PostMapping()
    public ResponseEntity<InviteResponseDto> inviteUser(@Validated @RequestBody InviteUserRequestDto inviteUserRequestDto) {
        Invite invite = inviteService.inviteUser(inviteUserRequestDto);
        return ResponseEntity.ok(InviteResponseDto.inviteResponseDto(invite));
    }

    @PostMapping("/list")
    public List<CombinedProjectListResponseDto> invitedProjectAndCreateProjectList(@Validated @RequestBody InvitedProjectMyProjectRequestDto invitedProjectMyProjectRequestDto) {
        return inviteService.invitedProjectAndCreateProjectList(invitedProjectMyProjectRequestDto);
    }
}
