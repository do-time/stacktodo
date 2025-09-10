package io.app.stacktodobe.task.application.service;

import io.app.stacktodobe.task.application.port.in.CreateTaskUseCase;
import io.app.stacktodobe.task.application.command.TaskCreateCommand;
import io.app.stacktodobe.workspace.application.port.out.WorkspaceQueryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateTaskService implements CreateTaskUseCase {
//    private final MemberQueryPort memberQueryPort;
    private final WorkspaceQueryPort workspaceQueryPort;
//    private final CategoryQueryPort categoryQueryPort;

    @Override
    public void create(TaskCreateCommand cmd) {

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
