package io.app.stacktodobe.workspace.mapper;

import io.app.stacktodobe.member.persistence.entity.Member;
import io.app.stacktodobe.workspace.adapter.in.web.dto.WorkspaceCreateRequestDto;
import io.app.stacktodobe.workspace.adapter.out.persistence.entity.WorkspaceEntity;
import io.app.stacktodobe.workspace.application.command.WorkspaceCreateCommand;
import io.app.stacktodobe.workspace.domain.model.Workspace;

import java.util.UUID;

public final class WorkspaceMapper {
    public static WorkspaceCreateCommand toCreateCommand(WorkspaceCreateRequestDto dto) {
        return new WorkspaceCreateCommand(dto.name(), dto.ownerPublicId());
    }

    public static Workspace toDomain(WorkspaceCreateCommand cmd, UUID workspaceId) {
        return new Workspace(workspaceId, cmd.name(), cmd.ownerPublicId());
    }

    public static WorkspaceEntity toEntity(WorkspaceCreateCommand cmd, UUID workspaceId, Member member) {
        return WorkspaceEntity.createWorkspace(cmd.name(), workspaceId, member);
    }
}
