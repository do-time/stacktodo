package io.app.stacktodobe.task.application.port.in;

import io.app.stacktodobe.task.application.command.TaskCreateCommand;

public interface TaskUseCase {

    public void createTask(TaskCreateCommand command);
}
