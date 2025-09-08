package io.app.stacktodobe.workspace.application.port.out;

import io.app.stacktodobe.workspace.adapter.in.web.dto.WorkspaceView;

import java.util.UUID;

public interface WorkspaceQueryPort {
    WorkspaceView findById(UUID workspaceId, UUID memberId);
    WorkspaceView findByName(String name);
}
