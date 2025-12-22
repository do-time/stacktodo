package io.app.stacktodobe.task.application.port.out;

import io.app.stacktodobe.task.domain.model.Task;

import java.util.Optional;
import java.util.UUID;

public interface DeleteTaskPort {
    Task deleteById(UUID taskId);

    Optional<Task> findById(UUID taskId);
}
