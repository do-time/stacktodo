package io.app.stacktodobe.task.application.port.out;

import io.app.stacktodobe.task.domain.model.Task;

public interface CreateTaskPort {
    Task create(Task task);
}
