package io.app.stacktodobe.task.adapter.out.persistence;

import io.app.stacktodobe.task.adapter.out.persistence.repository.TaskRepository;
import io.app.stacktodobe.task.application.port.out.CreateTaskPort;
import io.app.stacktodobe.task.domain.model.Task;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class TaskRepositoryAdapter implements CreateTaskPort{
    private final TaskRepository taskRepository;

    @Override
    public Task create(Task task) {
        return null;
    }
}
