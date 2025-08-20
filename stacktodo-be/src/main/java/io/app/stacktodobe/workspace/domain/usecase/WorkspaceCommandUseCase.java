package io.app.stacktodobe.workspace.domain.usecase;

import io.app.stacktodobe.workspace.presentation.command.WorkspaceCreateCommand;

public interface WorkspaceCommandUseCase {
    public void createWorkspace( WorkspaceCreateCommand command);
}
