package io.app.stacktodobe.task.application.port.in;

import io.app.stacktodobe.task.application.command.*;

import java.util.UUID;

public interface DeleteTaskUseCase {
    void delete(UUID taskId);

    void restore(UUID taskId);
}
