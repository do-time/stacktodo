package io.app.stacktodobe.workspace.command.support;

import io.app.stacktodobe.member.persistence.entity.Member;
import io.app.stacktodobe.member.persistence.repository.MemberRepository;
import io.app.stacktodobe.workspace.persistence.entity.Workspace;
import io.app.stacktodobe.workspace.persistence.repository.WorkspaceRepository;
import io.app.stacktodobe.workspace.presentation.command.WorkspaceCreateCommand;

import java.util.concurrent.ThreadLocalRandom;

public final class WorkspaceFixtures {

    public static WorkspaceCreateCommand validCreateCommand(Long ownerId) {
        return new WorkspaceCreateCommand(validName(), ownerId);
    }

    public static Workspace persisted(WorkspaceRepository workspaceRepository,
                                      MemberRepository memberRepository,
                                      Long ownerId){
        Member owner = memberRepository.findById(ownerId).orElseThrow();
        Workspace workspace = Workspace.createWorkspace(validName(), owner);

        return workspaceRepository.save(workspace);
    }

    public static String validName() {
        String random = Long.toString(ThreadLocalRandom.current().nextLong(Long.MAX_VALUE), 16); // 0-9a-z
        String name = "ws-" + random;
        return name.length() <= 100 ? name : name.substring(0, 100);
    }
}

