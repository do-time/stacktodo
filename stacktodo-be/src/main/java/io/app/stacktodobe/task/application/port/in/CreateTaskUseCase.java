package io.app.stacktodobe.task.application.port.in;

import io.app.stacktodobe.task.application.command.TaskCreateCommand;

public interface CreateTaskUseCase {
    void create(TaskCreateCommand cmd);
}
