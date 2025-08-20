package io.app.stacktodobe.workspace.domain.usecase;

import io.app.stacktodobe.workspace.presentation.command.WorkspaceCreateCommand;

public interface WorkspaceCommandUserCase {
    public void createWorkspace( WorkspaceCreateCommand command);
}
