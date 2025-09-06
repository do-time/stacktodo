package io.app.stacktodobe.workspace.adapter.out.persistence;


import io.app.stacktodobe.member.persistence.repository.MemberRepository;
import io.app.stacktodobe.workspace.adapter.out.persistence.entity.WorkspaceEntity;
import io.app.stacktodobe.workspace.adapter.out.persistence.repository.WorkspaceRepository;
import io.app.stacktodobe.workspace.application.port.out.CreateWorkspacePort;
import io.app.stacktodobe.workspace.domain.model.Workspace;
import io.app.stacktodobe.workspace.exception.InvalidCommandException;
import io.app.stacktodobe.workspace.mapper.WorkspaceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class WorkspaceRepositoryAdapter implements CreateWorkspacePort {
    private final WorkspaceRepository workspaceRepository;

    private final MemberRepository memberRepository;

    @Override
    public Workspace create(Workspace workspace) {
        // 1) member 검증
        var owner = memberRepository.findByMemberId(workspace.ownerPublicId())
                .orElseThrow(() -> new InvalidCommandException("존재하지 않는 사용자입니다."));

        // 2) workspace 중복 체크
        if (workspaceRepository.existsByOwner_IdAndNameIgnoreCase(owner.getId(), workspace.name())) {
            throw new InvalidCommandException("중복된 워크스페이스 명칭입니다.");
        }

        WorkspaceEntity entity = WorkspaceMapper.toEntity(workspace, owner);
        WorkspaceEntity created = workspaceRepository.save(entity);

        return WorkspaceMapper.toDomain(created);
    }
}
