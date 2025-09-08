package io.app.stacktodobe.workspace.application.port.in;

import io.app.stacktodobe.workspace.application.command.WorkspaceCreateCommand;

public interface WorkspaceCommandUseCase {
    public void create( WorkspaceCreateCommand command);
}
