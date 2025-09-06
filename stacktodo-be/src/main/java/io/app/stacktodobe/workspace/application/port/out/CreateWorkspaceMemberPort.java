package io.app.stacktodobe.workspace.application.port.out;

import io.app.stacktodobe.workspace.adapter.out.persistence.entity.WorkspaceMemberRole;

import java.util.UUID;

public interface CreateWorkspaceMemberPort {
    void create(UUID workspaceId, UUID memberId, WorkspaceMemberRole role);
}
