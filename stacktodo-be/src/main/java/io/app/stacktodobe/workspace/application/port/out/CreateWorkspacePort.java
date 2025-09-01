package io.app.stacktodobe.workspace.application.port.out;

import io.app.stacktodobe.workspace.domain.model.Workspace;

public interface CreateWorkspacePort {
    public Workspace create(Workspace workspace);
}
