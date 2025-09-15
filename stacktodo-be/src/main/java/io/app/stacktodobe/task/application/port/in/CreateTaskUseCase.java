package io.app.stacktodobe.task.application.port.in;

import io.app.stacktodobe.task.application.command.CreateTaskCommand;

import java.util.UUID;

public interface CreateTaskUseCase {
    void create(CreateTaskCommand cmd);
}
