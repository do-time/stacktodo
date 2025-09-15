package io.app.stacktodobe.task.application.service;

import io.app.stacktodobe.task.application.command.*;
import io.app.stacktodobe.task.application.port.in.CreateTaskUseCase;
import io.app.stacktodobe.task.application.port.in.UpdateTaskUseCase;
import io.app.stacktodobe.task.application.port.out.UpdateTaskPort;
import io.app.stacktodobe.workspace.application.port.out.WorkspaceQueryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

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

        task.changeDescription(cmd.description());
        updateTaskPort.save(task);
    }

    @Override
    public void updateTask(UpdateTaskCommand cmd) {
        var task = updateTaskPort.findById(cmd.taskId())
                .orElseThrow(() -> new IllegalArgumentException("Task not found: " + cmd.taskId()));

        // === 단순 속성 ===
        if(cmd.title() != null) {task.changeTitle(cmd.title());}
        if(cmd.description() != null) {task.changeDescription(cmd.description());}

        // === 캘린더 시작/종료 ===
        if (cmd.startDate() != null)    task.changeStartDate(cmd.startDate());
        if (cmd.startTime() != null)    task.changeStartTime(cmd.startTime());
        if (cmd.dueDate() != null)      task.changeDueDate(cmd.dueDate());
        if (cmd.dueTime() != null)      task.changeDueTime(cmd.dueTime());

        updateTaskPort.save(task);
    }
}
