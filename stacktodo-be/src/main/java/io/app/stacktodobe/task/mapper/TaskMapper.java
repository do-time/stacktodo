package io.app.stacktodobe.task.mapper;

import io.app.stacktodobe.task.adapter.in.web.dto.CreateTaskDto;
import io.app.stacktodobe.task.adapter.in.web.dto.TaskView;
import io.app.stacktodobe.task.adapter.in.web.dto.UpdateTaskDto;
import io.app.stacktodobe.task.adapter.out.persistence.entity.TaskEntity;
import io.app.stacktodobe.task.application.command.CompleteTaskCommand;
import io.app.stacktodobe.task.application.command.CreateTaskCommand;
import io.app.stacktodobe.task.application.command.UnCompleteTaskCommand;
import io.app.stacktodobe.task.application.command.UpdateTaskCommand;
import io.app.stacktodobe.task.domain.model.Task;

import java.util.UUID;

public class TaskMapper {
    public static CreateTaskCommand toCommand(CreateTaskDto dto) {
        return CreateTaskCommand.builder()
                .title(dto.title())
                .description(dto.description())
                .workspaceId(dto.workspaceId())
                .categoryId(dto.categoryId())
                .ownerId(dto.ownerId())
                .templateId(dto.templateId())
                .startTime(dto.startTime())
                .startDate(dto.startDate())
                .dueDate(dto.dueDate())
                .dueTime(dto.dueTime())
                .priority(dto.priority() != null ? dto.priority() : 3) // 기본값 3
                .isComplete(dto.isComplete() != null ? dto.isComplete() : false)
                .isRoutine(dto.isRoutine() != null ? dto.isRoutine() : false)
                .recurrenceRule(dto.recurrenceRule())
                .build();
    }

    public static CompleteTaskCommand toCompleteCommand(UUID taskId){
        return new CompleteTaskCommand(taskId);
    }

    public static UnCompleteTaskCommand toUnCompleteCommand(UUID taskId){
        return new UnCompleteTaskCommand(taskId);
    }

    public static UpdateTaskCommand toUpdateCommand(UUID taskId, UpdateTaskDto dto){
        return UpdateTaskCommand.builder()
                .taskId(taskId)
                .title(dto.title())
                .description(dto.description())
                .startTime(dto.startTime())
                .startDate(dto.startDate())
                .dueDate(dto.dueDate())
                .dueTime(dto.dueTime())
                .priority(dto.priority() != null ? dto.priority() : 3)
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
                entity.getStartDate(),
                entity.getStartTime(),
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
                .startDate(task.getStartDate())
                .startTime(task.getStartTime())
                .dueDate(task.getDueDate())
                .dueTime(task.getDueTime())
                .priority(task.getPriority())
                .isRoutine(task.isRoutine())
                .recurrenceRule(task.getRecurrenceRule())
                .status(task.getStatus())
                .build();
    }

    public static TaskView toView(Task task){
        return TaskView.builder()
                .taskId(task.getTaskId())
                .workspaceId(task.getWorkspaceId())
                .categoryId(task.getCategoryId())
                .ownerId(task.getOwnerId())
                .title(task.getTitle())
                .description(task.getDescription())
                .startDate(task.getStartDate())
                .startTime(task.getStartTime())
                .dueDate(task.getDueDate())
                .dueTime(task.getDueTime())
                .priority(task.getPriority())
                .status(task.getStatus())
                .percentComplete(task.getPercentComplete())
                .recurrenceRule(task.getRecurrenceRule())
                .templateCategoryId(task.getTemplateCategoryId())
                .build();
    }
}
