package com.milvus.vector_spring.invite;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InviteRepository extends JpaRepository<Invite, Long> {
    Optional<Invite> findOneForBanish(Long projectId, Long basnishId);
}
