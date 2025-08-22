package io.app.stacktodobe.task.adapter.command;

import io.app.stacktodobe.task.persistence.entity.Task;
import io.app.stacktodobe.task.presentation.command.TaskCreateCommand;
import lombok.RequiredArgsConstructor;

import java.util.function.Consumer;

@RequiredArgsConstructor
public class TaskCommandExecutor {

    private final Consumer<Task> saveWorkspace;

    public void execute(TaskCreateCommand command) {
        //saveWorkspace.accept(Workspace.createWorkspace(command));
    }

    public void validate(TaskCreateCommand command) {
        //
    }

}
