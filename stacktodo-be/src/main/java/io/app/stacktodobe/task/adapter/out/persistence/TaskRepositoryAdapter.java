package io.app.stacktodobe.task.adapter.out.persistence;

import io.app.stacktodobe.task.adapter.out.persistence.repository.TaskRepository;
import io.app.stacktodobe.task.application.port.out.CreateTaskPort;
import io.app.stacktodobe.task.application.port.out.UpdateTaskPort;
import io.app.stacktodobe.task.domain.model.Task;
import io.app.stacktodobe.task.mapper.TaskMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
@Slf4j
public class TaskRepositoryAdapter implements CreateTaskPort, UpdateTaskPort {
    private final TaskRepository taskRepository;

    @Override
    public Task create(Task task) {
        var created = taskRepository.save(TaskMapper.toEntity(task));
        return TaskMapper.toDomain(created);
    }

    @Override
    public Task save(Task task) {
        var saved = taskRepository.save(TaskMapper.toEntity(task));
        return TaskMapper.toDomain(saved);
    }

    @Override
    public Optional<Task> findById(UUID taskId) {
        return taskRepository.findByTaskId(taskId).map(TaskMapper::toDomain);
    }
}
