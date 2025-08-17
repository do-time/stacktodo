package io.app.stacktodobe.workspace.domain.usecase;

import io.app.stacktodobe.member.persistence.entity.Member;
import io.app.stacktodobe.member.persistence.repository.MemberRepository;
import io.app.stacktodobe.workspace.adapter.command.WorkspaceCommandExecutor;
import io.app.stacktodobe.workspace.adapter.model.InvalidCommandException;
import io.app.stacktodobe.workspace.persistence.entity.Workspace;
import io.app.stacktodobe.workspace.persistence.repository.WorkspaceRepository;
import io.app.stacktodobe.workspace.presentation.command.WorkspaceCreateCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
public class WorkspaceCommandUserCase {
    private final WorkspaceRepository workspaceRepository;

    private final MemberRepository memberRepository;

    public void createWorkspace(@RequestBody WorkspaceCreateCommand command) {
        Member owner = memberRepository.findById(command.ownerId())
                .orElseThrow(() -> new InvalidCommandException("존재하지 않는 사용자입니다."));

        if (workspaceRepository.existsByOwner_IdAndNameIgnoreCase(command.ownerId(), command.name())) {
            throw new InvalidCommandException("중복된 워크스페이스 명칭입니다.");
        }

        workspaceRepository.save(Workspace.createWorkspace(command.name(), owner));

        //executor로 한 번 더 감싸서 save를 하는 이유를 아직 모르겠음..
        //WorkspaceCommandExecutor executor = new WorkspaceCommandExecutor(workspaceRepository::save);
        //executor.execute(command);
    }
}
