package io.app.stacktodobe.task.application.command;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ChangeDescriptionCommand(
        @NotNull
        UUID taskId,
        String description
) {
}
