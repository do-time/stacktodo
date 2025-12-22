package io.app.stacktodobe.task.adapter.out.persistence;

import io.app.stacktodobe.common.exception.EntityNotFoundException;
import io.app.stacktodobe.task.adapter.out.persistence.entity.TaskEntity;
import io.app.stacktodobe.task.adapter.out.persistence.repository.TaskRepository;
import io.app.stacktodobe.task.application.port.out.CreateTaskPort;
import io.app.stacktodobe.task.application.port.out.DeleteTaskPort;
import io.app.stacktodobe.task.application.port.out.TaskQueryPort;
import io.app.stacktodobe.task.application.port.out.UpdateTaskPort;
import io.app.stacktodobe.task.domain.model.Task;
import io.app.stacktodobe.task.mapper.TaskMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
@Slf4j
public class TaskRepositoryAdapter implements CreateTaskPort, UpdateTaskPort, DeleteTaskPort, TaskQueryPort {
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
    public Task update(Task task) {
        TaskEntity updated = findEntityById(task.getTaskId());

        updated.setTitle(task.getTitle());
        updated.setDescription(task.getDescription());
        updated.setStartDate(task.getStartDate());
        updated.setStartTime(task.getStartTime());
        updated.setDueDate(task.getDueDate());
        updated.setDueTime(task.getDueTime());
        updated.setPriority(task.getPriority());
        updated.setStatus(task.getStatus());
        updated.setPercentComplete(task.getPercentComplete());

        return TaskMapper.toDomain(updated);
    }

    @Override
    public Task deleteById(UUID taskId) {
        TaskEntity deleted = findEntityById(taskId);

        deleted.markDeleted(1234L);

        return TaskMapper.toDomain(deleted);
    }

    @Override
    public Optional<Task> findById(UUID taskId) {
        return taskRepository.findByTaskId(taskId).map(TaskMapper::toDomain);
    }

    @Override
    public Optional<Task> findByTaskIdAndOwnerId(UUID taskId, UUID ownerId) {
        return taskRepository.findByTaskIdAndOwnerId(taskId, ownerId).map(TaskMapper::toDomain);
    }

    @Override
    public List<Task> findAllByOwnerId(UUID ownerId) {
        return taskRepository.findAllByOwnerId(ownerId)
                .stream()
                .map(TaskMapper::toDomain)
                .toList();
    }

    @Override
    public List<Task> findAllByWorkspaceId(UUID workspaceId) {
        return List.of();
    }

    @Override
    public List<Task> findAllByOwnerIdAndStartDate(UUID ownerId, LocalDate date) {
        return taskRepository.findAllByOwnerIdAndStartDateAndDeletedFalse(ownerId, date)
                .stream()
                .map(TaskMapper::toDomain)
                .toList();
    }

    @Override
    public List<Task> findAllByOwnerIdAndMonth(UUID ownerId, YearMonth month) {
        return List.of();
    }

    private TaskEntity findEntityById(UUID TaskId){
        return taskRepository.findByTaskId(TaskId)
                .orElseThrow(() -> new EntityNotFoundException("TaskEntity not found: " + TaskId));
    }
}
