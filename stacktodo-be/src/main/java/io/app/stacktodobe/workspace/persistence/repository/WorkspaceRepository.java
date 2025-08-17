package io.app.stacktodobe.workspace.persistence.repository;

import io.app.stacktodobe.workspace.persistence.entity.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {
    boolean existsByOwner_IdAndNameIgnoreCase(Long ownerId, String name);
}
