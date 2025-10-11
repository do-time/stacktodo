package io.app.stacktodobe.workspace.adapter.out.persistence;


import io.app.stacktodobe.common.exception.EntityNotFoundException;
import io.app.stacktodobe.member.adapter.out.persistence.repository.MemberRepository;
import io.app.stacktodobe.workspace.adapter.out.persistence.entity.WorkspaceMemberEntity;
import io.app.stacktodobe.workspace.adapter.out.persistence.entity.WorkspaceMemberRole;
import io.app.stacktodobe.workspace.adapter.out.persistence.repository.WorkspaceMemberRepository;
import io.app.stacktodobe.workspace.adapter.out.persistence.repository.WorkspaceRepository;
import io.app.stacktodobe.workspace.application.port.out.CreateWorkspaceMemberPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
@Slf4j
public class WorkspaceMemberRepositoryAdapter implements CreateWorkspaceMemberPort {
    private final WorkspaceMemberRepository workspaceMemberRepository;
    private final WorkspaceRepository workspaceRepository;
    private final MemberRepository memberRepository;

    @Override
    public void create(UUID workspaceId, UUID memberId, WorkspaceMemberRole role) {
        var member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 사용자입니다."));

        var workspace = workspaceRepository.findByWorkspaceId(workspaceId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 워크스페이스입니다."));

        var entity = WorkspaceMemberEntity.of(workspace, member.getMemberId(), role);
        var saved = workspaceMemberRepository.save(entity);
    }
}
