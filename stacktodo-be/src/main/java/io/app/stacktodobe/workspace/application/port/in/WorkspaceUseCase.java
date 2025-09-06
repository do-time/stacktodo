package io.app.stacktodobe.workspace.application.port.in;

import io.app.stacktodobe.workspace.application.command.WorkspaceCreateCommand;

public interface WorkspaceUseCase {
    public void create( WorkspaceCreateCommand command);
}
