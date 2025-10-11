package io.app.stacktodobe.task.application.command;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UnCompleteTaskCommand(
        @NotNull
        UUID taskId
) {
}
