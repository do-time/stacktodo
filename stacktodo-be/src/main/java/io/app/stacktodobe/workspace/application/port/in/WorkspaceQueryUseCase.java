package io.app.stacktodobe.workspace.application.port.in;

import io.app.stacktodobe.workspace.adapter.in.web.dto.WorkspaceView;

import java.util.List;
import java.util.UUID;

public interface WorkspaceQueryUseCase {
    WorkspaceView getWorkspace(UUID workspaceId,  UUID memberId);
    WorkspaceView getById(UUID workspaceId,   UUID memberId);
    WorkspaceView getByName(String workspaceName, UUID memberId);
    List<WorkspaceView> listByOwnerId(UUID ownerId);
}
