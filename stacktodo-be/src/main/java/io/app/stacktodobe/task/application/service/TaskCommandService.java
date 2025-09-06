package io.app.stacktodobe.task.application.service;

import io.app.stacktodobe.category.persistence.repository.CategoryRepository;
import io.app.stacktodobe.member.adapter.out.persistence.entity.MemberEntity;
import io.app.stacktodobe.member.adapter.out.persistence.repository.MemberRepository;
import io.app.stacktodobe.member.application.port.in.MemberQueryPort;
import io.app.stacktodobe.task.exception.InvalidCommandException;
import io.app.stacktodobe.task.adapter.out.persistence.repository.TaskRepository;
import io.app.stacktodobe.task.application.port.in.TaskCommandUseCase;
import io.app.stacktodobe.task.application.command.TaskCreateCommand;
import io.app.stacktodobe.workspace.adapter.out.persistence.entity.WorkspaceEntity;
import io.app.stacktodobe.workspace.adapter.out.persistence.repository.WorkspaceRepository;
import io.app.stacktodobe.workspace.application.port.out.CreateWorkspacePort;
import io.app.stacktodobe.workspace.mapper.WorkspaceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskCommandService implements TaskCommandUseCase {
//    private final MemberQueryPort memberQueryPort;
//    private final WorkspaceQueryPort workspaceQueryPort;
//    private final CategoryQueryPort categoryQueryPort;

    @Override
    public void create(TaskCreateCommand cmd) {
//        UUID taskPublicId = UUID.randomUUID();
//        UUID workspacePublicId = workspaceQueryPort.findPublicIdById(cmd.workspaceId());
//        UUID ownerPublicId = memberQueryPort.findPublicIdById(cmd.ownerId());
//
//        UUID categoryPublicId = (cmd.categoryId() != null)
//                ? categoryQueryPort.findPublicIdById(cmd.categoryId())
//                : null;
//
//        UUID templatePublicId = (cmd.templateId() != null)
//                ? categoryQueryPort.findPublicIdById(cmd.templateId())
//                : null;
    }
}
