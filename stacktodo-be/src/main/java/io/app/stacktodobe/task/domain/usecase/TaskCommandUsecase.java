package io.app.stacktodobe.task.domain.usecase;

import io.app.stacktodobe.task.presentation.command.TaskCreateCommand;

public interface TaskCommandUsecase {

    public void createTask(TaskCreateCommand command);
}
