package io.app.stacktodobe.workspace.adapter.out.persistence.repository;

import io.app.stacktodobe.workspace.adapter.out.persistence.entity.WorkspaceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkspaceRepository extends JpaRepository<WorkspaceEntity, Long> {
    boolean existsByOwner_IdAndNameIgnoreCase(Long ownerId, String name);
}
