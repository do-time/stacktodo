package io.app.stacktodobe.workspace.adapter.out.persistence.repository;

import io.app.stacktodobe.workspace.adapter.out.persistence.entity.WorkspaceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface WorkspaceRepository extends JpaRepository<WorkspaceEntity, Long> {
    boolean existsByOwner_IdAndNameIgnoreCase(Long ownerId, String name);
    Optional<WorkspaceEntity> findByWorkspaceId(UUID workspaceId);
}
