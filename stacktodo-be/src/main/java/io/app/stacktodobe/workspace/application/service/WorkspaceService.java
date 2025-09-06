package io.app.stacktodobe.workspace.application.service;

import io.app.stacktodobe.workspace.adapter.out.persistence.entity.WorkspaceMemberRole;
import io.app.stacktodobe.workspace.application.command.WorkspaceCreateCommand;
import io.app.stacktodobe.workspace.application.port.in.WorkspaceUseCase;
import io.app.stacktodobe.workspace.application.port.out.CreateWorkspaceMemberPort;
import io.app.stacktodobe.workspace.application.port.out.CreateWorkspacePort;
import io.app.stacktodobe.workspace.mapper.WorkspaceMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class WorkspaceService implements WorkspaceUseCase {
    //member port 추가

    private final CreateWorkspacePort createWorkspacePort;

    private final CreateWorkspaceMemberPort createWorkspaceMemberPort;

    @Transactional
    public void create(WorkspaceCreateCommand cmd) {
        //member entity get 소스추가

        var workspace = WorkspaceMapper.toDomain(cmd, UUID.randomUUID());

        var savedWorkspace = createWorkspacePort.create(workspace);

        createWorkspaceMemberPort.create(workspace.workspaceId(), workspace.ownerPublicId(), WorkspaceMemberRole.OWNER);
    }
}
