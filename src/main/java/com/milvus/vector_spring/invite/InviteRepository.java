package com.milvus.vector_spring.invite;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InviteRepository extends JpaRepository<Invite, Long> {
    Optional<Invite> findOneForBanish(Long projectId, Long basnishId);
    Optional<List<Invite>> invitedUserList(Long invitedId);
    Optional<List<Invite>> projectInvitedUserList(Long invitedId, Long projectId);
}
