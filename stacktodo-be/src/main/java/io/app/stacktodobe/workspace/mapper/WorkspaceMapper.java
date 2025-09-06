package io.app.stacktodobe.workspace.mapper;

import io.app.stacktodobe.member.adapter.out.persistence.entity.MemberEntity;
import io.app.stacktodobe.workspace.adapter.in.web.dto.CreateWorkspaceDto;
import io.app.stacktodobe.workspace.adapter.out.persistence.entity.WorkspaceEntity;
import io.app.stacktodobe.workspace.application.command.WorkspaceCreateCommand;
import io.app.stacktodobe.workspace.domain.model.Workspace;

import java.util.UUID;

public final class WorkspaceMapper {
    public static WorkspaceCreateCommand toCreateCommand(CreateWorkspaceDto dto) {
        return new WorkspaceCreateCommand(dto.name(), dto.ownerPublicId());
    }

    public static Workspace toDomain(WorkspaceCreateCommand cmd, UUID workspaceId) {
        return new Workspace(workspaceId, cmd.name(), cmd.ownerPublicId());
    }

    public static Workspace toDomain(WorkspaceEntity entity) {
        return Workspace.of(entity.getWorkspaceId(), entity.getName(), entity.getOwner().getMemberId());
    }

    public static WorkspaceEntity toEntity(Workspace workspace, MemberEntity memberEntity) {
        return WorkspaceEntity.createWorkspace(workspace.name(), workspace.workspaceId(), memberEntity);
    }
}
