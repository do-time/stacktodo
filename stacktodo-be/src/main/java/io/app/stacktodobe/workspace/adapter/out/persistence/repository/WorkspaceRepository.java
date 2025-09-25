package io.app.stacktodobe.workspace.adapter.out.persistence.repository;

import io.app.stacktodobe.workspace.adapter.out.persistence.entity.WorkspaceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WorkspaceRepository extends JpaRepository<WorkspaceEntity, Long> {
    boolean existsByOwnerIdAndNameIgnoreCase(UUID ownerId, String name);
    Optional<WorkspaceEntity> findByWorkspaceId(UUID workspaceId);
    Optional<WorkspaceEntity> findByNameAndOwnerId(String name, UUID ownerId);
    List<WorkspaceEntity> findAllByOwnerId(UUID ownerId);
}
