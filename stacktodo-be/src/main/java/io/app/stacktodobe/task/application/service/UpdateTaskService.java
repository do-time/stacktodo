package io.app.stacktodobe.task.application.service;

import io.app.stacktodobe.task.application.command.*;
import io.app.stacktodobe.task.application.port.in.CreateTaskUseCase;
import io.app.stacktodobe.task.application.port.in.UpdateTaskUseCase;
import io.app.stacktodobe.task.application.port.out.UpdateTaskPort;
import io.app.stacktodobe.workspace.application.port.out.WorkspaceQueryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateTaskService implements UpdateTaskUseCase {
    private final UpdateTaskPort updateTaskPort;

    @Override
    public void complete(CompleteTaskCommand cmd) {
        var task = updateTaskPort.findById(cmd.taskId())
                .orElseThrow(() -> new IllegalArgumentException("Task not found: " + cmd.taskId()));

        task.complete();
        updateTaskPort.save(task);
    }

    @Override
    public void unComplete(UnCompleteTaskCommand cmd) {
        var task = updateTaskPort.findById(cmd.taskId())
                .orElseThrow(() -> new IllegalArgumentException("Task not found: " + cmd.taskId()));

        task.unComplete();
        updateTaskPort.save(task);
    }

    @Override
    public void changeTitle(ChangeTitleCommand cmd) {
        var task = updateTaskPort.findById(cmd.taskId())
                .orElseThrow(() -> new IllegalArgumentException("Task not found: " + cmd.taskId()));

        task.changeTitle(cmd.title());
        updateTaskPort.save(task);
    }

    @Override
    public void changeDescription(ChangeDescriptionCommand cmd) {
        var task = updateTaskPort.findById(cmd.taskId())
                .orElseThrow(() -> new IllegalArgumentException("Task not found: " + cmd.taskId()));

        task.changeTitle(cmd.description());
        updateTaskPort.save(task);
    }
}
