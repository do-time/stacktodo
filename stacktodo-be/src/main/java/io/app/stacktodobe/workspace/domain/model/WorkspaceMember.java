package io.app.stacktodobe.workspace.domain.model;

import io.app.stacktodobe.workspace.adapter.out.persistence.entity.WorkspaceMemberRole;

import java.util.UUID;

public record WorkspaceMember(
        UUID workspaceId,
        UUID memberId,
        WorkspaceMemberRole role
) {
    public static WorkspaceMember of(UUID workspaceId, UUID memberId, WorkspaceMemberRole role) {
        return new WorkspaceMember(workspaceId, memberId, role);
    }
}
