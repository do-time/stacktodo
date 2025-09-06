package io.app.stacktodobe.workspace.application.service;

import io.app.stacktodobe.member.persistence.entity.Member;
import io.app.stacktodobe.member.persistence.repository.MemberRepository;
import io.app.stacktodobe.workspace.application.port.in.WorkspaceUseCase;
import io.app.stacktodobe.workspace.application.port.out.CreateWorkspacePort;
import io.app.stacktodobe.workspace.domain.model.Workspace;
import io.app.stacktodobe.workspace.exception.InvalidCommandException;
import io.app.stacktodobe.workspace.adapter.out.persistence.entity.WorkspaceEntity;
import io.app.stacktodobe.workspace.adapter.out.persistence.entity.WorkspaceMemberEntity;
import io.app.stacktodobe.workspace.adapter.out.persistence.entity.WorkspaceMemberRole;
import io.app.stacktodobe.workspace.adapter.out.persistence.repository.WorkspaceMemberRepository;
import io.app.stacktodobe.workspace.adapter.out.persistence.repository.WorkspaceRepository;
import io.app.stacktodobe.workspace.application.command.WorkspaceCreateCommand;
import io.app.stacktodobe.workspace.mapper.WorkspaceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkspaceService implements WorkspaceUseCase {
    //member port 추가

    private final CreateWorkspacePort createWorkspacePort;

    @Transactional
    public void create(WorkspaceCreateCommand cmd) {
        //member entity get 소스추가

        var workspace = WorkspaceMapper.toDomain(cmd, UUID.randomUUID());

        var savedWorkspace = createWorkspacePort.create(workspace);
    }
}
