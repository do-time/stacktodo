package io.app.stacktodobe.workspace.application.port.in;

import io.app.stacktodobe.workspace.adapter.in.web.dto.WorkspaceView;

import java.util.UUID;

public interface WorkspaceQueryUseCase {
    public WorkspaceView getWorkspace(UUID workspaceId,  UUID memberId);
    public WorkspaceView getWorkspace(String workspaceName);
}
