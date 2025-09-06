package io.app.stacktodobe.task.mapper;

import io.app.stacktodobe.task.adapter.in.web.dto.CreateTaskDto;
import io.app.stacktodobe.task.application.command.TaskCreateCommand;
import io.app.stacktodobe.task.domain.model.Task;

import java.util.UUID;

public class TaskMapper {
    public static TaskCreateCommand toCommand(CreateTaskDto dto){
        return TaskCreateCommand.of(dto);
    }

    public static Task toDomain(UUID publicId,
                                UUID workspacePublicId,
                                UUID ownerPublicId,
                                UUID categoryPublicId,
                                UUID templateCategoryPublicId,
                                TaskCreateCommand cmd){
        return Task.of(publicId, workspacePublicId, ownerPublicId, categoryPublicId, templateCategoryPublicId, cmd);
    }
}
