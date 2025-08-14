package io.app.stacktodobe.workspace.service;

import io.app.stacktodobe.member.entity.Member;
import io.app.stacktodobe.workspace.entity.Workspace;
import io.app.stacktodobe.workspace.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WorkspaceServiceImpl implements WorkspaceService {

    private final WorkspaceRepository workspaceRepository;

    @Override
    @Transactional
    public void createWorkspace() {
        // memberService에서 받아올 데이터
        Member member = new Member();

        var newWorkspace = Workspace.builder()
                .owner(member)
                .name(member.getName())
                .build();

        workspaceRepository.save(newWorkspace);
    }
}
