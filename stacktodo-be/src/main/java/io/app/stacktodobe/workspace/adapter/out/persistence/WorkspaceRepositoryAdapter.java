package io.app.stacktodobe.workspace.adapter.out.persistence;


import io.app.stacktodobe.common.exception.EntityNotFoundException;
import io.app.stacktodobe.member.adapter.out.persistence.repository.MemberRepository;
import io.app.stacktodobe.workspace.adapter.in.web.dto.WorkspaceView;
import io.app.stacktodobe.workspace.adapter.out.persistence.repository.WorkspaceRepository;
import io.app.stacktodobe.workspace.application.port.out.CreateWorkspacePort;
import io.app.stacktodobe.workspace.application.port.out.WorkspaceQueryPort;
import io.app.stacktodobe.workspace.domain.model.Workspace;
import io.app.stacktodobe.workspace.mapper.WorkspaceMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
@Slf4j
public class WorkspaceRepositoryAdapter implements CreateWorkspacePort, WorkspaceQueryPort {
    private final WorkspaceRepository workspaceRepository;

    private final MemberRepository memberRepository;

    @Override
    public Workspace create(Workspace workspace) {
        // 1) member 검증 빼야함
        var owner = memberRepository.findByMemberId(workspace.ownerId())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 사용자입니다."));

        var entity = WorkspaceMapper.toEntity(workspace, owner.getMemberId());
        workspace.members().forEach(m ->
                entity.addMember(m.memberId(), m.role())
        );

        var created = workspaceRepository.save(entity);

        return WorkspaceMapper.toDomain(created);
    }

    @Override
    public WorkspaceView findById(UUID workspaceId, UUID memberId) {
        return workspaceRepository.findByWorkspaceId(workspaceId)
                .map(WorkspaceMapper::toView)
                .orElseThrow(() -> new EntityNotFoundException("workspace id not found: " + workspaceId));
    }

    @Override
    public WorkspaceView findByName(String name, UUID memberId) {
        return workspaceRepository.findByNameAndOwnerId(name, memberId)
                .map(WorkspaceMapper::toView)
                .orElseThrow(() -> new EntityNotFoundException("workspace name not found: " + name));
    }
    @Override
    public List<WorkspaceView> findAllByOwnerId(UUID ownerId) {
        return workspaceRepository.findAllByOwnerId(ownerId)
                .stream()
                .map(WorkspaceMapper::toView)
                .toList();
    }

    @Override
    public boolean existsByOwnerIdAndNameIgnoreCase(UUID ownerId, String name) {
        return workspaceRepository.existsByOwnerIdAndNameIgnoreCase(ownerId, name);
    }
}
