package io.app.stacktodobe.workspace.application.service;

import io.app.stacktodobe.member.application.port.in.MemberQueryPort;
import io.app.stacktodobe.workspace.adapter.out.persistence.entity.WorkspaceMemberRole;
import io.app.stacktodobe.workspace.application.command.WorkspaceCreateCommand;
import io.app.stacktodobe.workspace.application.port.in.WorkspaceCommandUseCase;
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
public class WorkspaceCommandService implements WorkspaceCommandUseCase {
    //member port 추가
    //private final MemberQueryPort memberQueryPort;

    private final CreateWorkspacePort createWorkspacePort;

    private final CreateWorkspaceMemberPort createWorkspaceMemberPort;

    @Transactional
    public void create(WorkspaceCreateCommand cmd) {
        //member entity get 소스추가
        //var member =

        var workspace = WorkspaceMapper.toDomain(
                cmd,
                UUID.randomUUID()
        );
        workspace.invite(cmd.ownerId(), WorkspaceMemberRole.OWNER);

        var savedWorkspace = createWorkspacePort.create(workspace);
    }
}
