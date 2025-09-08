package io.app.stacktodobe.workspace.adapter.in.web.dto;

import io.app.stacktodobe.workspace.adapter.out.persistence.entity.WorkspaceEntity;
import io.app.stacktodobe.workspace.domain.model.Workspace;
import lombok.Builder;

import java.util.UUID;

@Builder
public record WorkspaceView(
        UUID workspaceId,
        UUID ownerId,
        String name
) {
    public static WorkspaceView of(UUID workspaceId, UUID ownerId, String name) {
        return WorkspaceView.builder()
                .workspaceId(workspaceId)
                .ownerId(ownerId)
                .name(name)
                .build();
    }

    public static WorkspaceView from(WorkspaceEntity entity) {
        return WorkspaceView.builder()
                .workspaceId(entity.getWorkspaceId())
                .name(entity.getName())
                .ownerId(entity.getOwnerId())
                .build();
    }
}
