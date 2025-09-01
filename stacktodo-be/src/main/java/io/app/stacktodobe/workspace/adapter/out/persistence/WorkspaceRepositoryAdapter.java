package io.app.stacktodobe.workspace.adapter.out.persistence;


import io.app.stacktodobe.workspace.adapter.out.persistence.entity.WorkspaceEntity;
import io.app.stacktodobe.workspace.adapter.out.persistence.repository.WorkspaceRepository;
import io.app.stacktodobe.workspace.application.port.out.CreateWorkspacePort;
import io.app.stacktodobe.workspace.domain.model.Workspace;
import io.app.stacktodobe.workspace.mapper.WorkspaceMapper;

public class WorkspaceRepositoryAdapter implements CreateWorkspacePort {

    private final WorkspaceRepository workspaceRepository;

    @Override
    public Workspace create(Workspace workspace) {
        WorkspaceEntity workspaceEntity = WorkspaceMapper.toCreateCommand(workspace);
        return null;
    }
}
