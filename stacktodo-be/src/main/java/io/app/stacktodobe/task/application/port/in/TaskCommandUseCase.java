package io.app.stacktodobe.task.application.port.in;

import io.app.stacktodobe.task.application.command.TaskCreateCommand;

public interface TaskCommandUseCase {
    public void create(TaskCreateCommand cmd);
}
