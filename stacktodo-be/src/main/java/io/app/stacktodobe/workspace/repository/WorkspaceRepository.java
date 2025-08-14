package io.app.stacktodobe.workspace.repository;

import io.app.stacktodobe.workspace.entity.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {

}
