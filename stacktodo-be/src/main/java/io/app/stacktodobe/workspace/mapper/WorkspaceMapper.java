package io.app.stacktodobe.workspace.mapper;

import io.app.stacktodobe.workspace.adapter.in.web.dto.CreateWorkspaceDto;
import io.app.stacktodobe.workspace.adapter.in.web.dto.WorkspaceView;
import io.app.stacktodobe.workspace.adapter.out.persistence.entity.WorkspaceEntity;
import io.app.stacktodobe.workspace.application.command.WorkspaceCreateCommand;
import io.app.stacktodobe.workspace.domain.model.Workspace;

import java.util.UUID;

public final class WorkspaceMapper {
    public static WorkspaceCreateCommand toCreateCommand(CreateWorkspaceDto dto) {
        return WorkspaceCreateCommand.of(dto.name(), dto.ownerId());
    }

    public static Workspace toDomain(WorkspaceCreateCommand cmd, UUID workspaceId) {
        return Workspace.of(workspaceId, cmd.name(), cmd.ownerId());
    }

    public static Workspace toDomain(WorkspaceEntity entity) {
        Workspace workspace = Workspace.of(entity.getWorkspaceId(), entity.getName(), entity.getOwnerId());

        if(entity.getMembers() != null) {
            entity.getMembers().forEach(m -> {
                workspace.invite(m.getMemberId(), m.getRole());
            });
        }

        return workspace;
    }

    public static WorkspaceEntity toEntity(Workspace workspace, UUID memberId) {
        return WorkspaceEntity.of(workspace.name(), workspace.workspaceId(), memberId);
    }

    public static WorkspaceView toView(WorkspaceEntity entity) {
        return WorkspaceView.from(entity);
    }
}
