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
    private final WorkspaceRepository workspaceRepository;

    private final MemberRepository memberRepository;

    private final CreateWorkspacePort createWorkspacePort;

    @Transactional
    public void create(WorkspaceCreateCommand cmd) {
        // 1) member 검증
        var owner = memberRepository.findByMemberId(cmd.ownerPublicId())
                .orElseThrow(() -> new InvalidCommandException("존재하지 않는 사용자입니다."));

        // 2) workspace 중복 체크
        if (workspaceRepository.existsByOwner_IdAndNameIgnoreCase(owner.getId(), cmd.name())) {
            throw new InvalidCommandException("중복된 워크스페이스 명칭입니다.");
        }

        var workspace = WorkspaceMapper.toDomain(cmd, UUID.randomUUID());

        // 3) workspace 저장
        var savedWorkspace = createWorkspacePort.create(workspace);
    }
}
