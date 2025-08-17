package io.app.stacktodobe.workspace.presentation.command;

public record WorkspaceCreateCommand(
        @jakarta.validation.constraints.NotBlank
        @jakarta.validation.constraints.Size(max = 100)
        String name,

        @jakarta.validation.constraints.NotNull
        Long ownerId
) {}
