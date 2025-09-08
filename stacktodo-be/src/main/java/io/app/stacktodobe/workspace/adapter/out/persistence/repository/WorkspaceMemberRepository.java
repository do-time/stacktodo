package io.app.stacktodobe.workspace.adapter.out.persistence.repository;

import io.app.stacktodobe.workspace.adapter.out.persistence.entity.WorkspaceMemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface WorkspaceMemberRepository extends JpaRepository<WorkspaceMemberEntity, Long> {
    Optional<WorkspaceMemberEntity> findByWorkspaceIdAndMemberId(Long workspaceId, UUID memberId);
    boolean existsByWorkspaceIdAndMemberId(Long workspaceId, UUID memberId);
}
