package io.app.stacktodobe.task.application.service;

import io.app.stacktodobe.task.application.port.in.TaskCommandUseCase;
import io.app.stacktodobe.task.application.command.TaskCreateCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
