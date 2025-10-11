package io.app.stacktodobe.task.application.service;

import io.app.stacktodobe.task.application.port.in.CreateTaskUseCase;
import io.app.stacktodobe.task.application.command.CreateTaskCommand;
import io.app.stacktodobe.task.application.port.out.CreateTaskPort;
import io.app.stacktodobe.task.domain.model.Task;
import io.app.stacktodobe.workspace.application.port.out.WorkspaceQueryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateTaskService implements CreateTaskUseCase {
    private final CreateTaskPort createTaskPort;

    @Override
    public void create(CreateTaskCommand cmd) {

        //member 검증

        //workspace 검증

        //category 검증

        var task = Task.create(cmd);

        var created = createTaskPort.create(task);
    }
}
