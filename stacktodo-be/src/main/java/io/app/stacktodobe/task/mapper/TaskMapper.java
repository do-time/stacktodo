package io.app.stacktodobe.task.mapper;

import io.app.stacktodobe.task.adapter.in.web.dto.CreateTaskDto;
import io.app.stacktodobe.task.adapter.out.persistence.entity.TaskEntity;
import io.app.stacktodobe.task.application.command.TaskCreateCommand;
import io.app.stacktodobe.task.domain.model.Task;

import java.util.UUID;

public class TaskMapper {
    public static TaskCreateCommand toCommand(CreateTaskDto dto) {
        return TaskCreateCommand.builder()
                .title(dto.title())
                .description(dto.description())
                .workspaceId(dto.workspaceId())
                .categoryId(dto.categoryId())
                .ownerId(dto.ownerId())
                .templateId(dto.templateId())
                .dueDate(dto.dueDate())
                .dueTime(dto.dueTime())
                .priority(dto.priority() != null ? dto.priority() : 3) // 기본값 3
                .isComplete(dto.isComplete() != null ? dto.isComplete() : false)
                .isRoutine(dto.isRoutine() != null ? dto.isRoutine() : false)
                .recurrenceRule(dto.recurrenceRule())
                .build();
    }

    public static Task toDomain(TaskEntity entity) {
        return Task.reconstruct(
                entity.getTaskId(),
                entity.getWorkspaceId(),
                entity.getCategoryId(),
                entity.getOwnerId(),
                null,
                entity.getTitle(),
                entity.getDescription(),
                entity.getDueDate(),
                entity.getDueTime(),
                entity.getPriority(),
                entity.getPercentComplete(),
                entity.isRoutine(),
                entity.getRecurrenceRule(),
                entity.getStatus()
        );
    }

    public static TaskEntity toEntity(Task task) {
        return TaskEntity.builder()
                .title(task.getTitle())
                .description(task.getDescription())
                .workspaceId(task.getWorkspaceId())
                .categoryId(task.getCategoryId())
                .ownerId(task.getOwnerId())
                //.templateId(task.templateId())
                .dueDate(task.getDueDate())
                .dueTime(task.getDueTime())
                .priority(task.getPriority())
                .isRoutine(task.isRoutine())
                .recurrenceRule(task.getRecurrenceRule())
                .status(task.getStatus())
                .build();
    }
}
