package io.app.stacktodobe.workspace.application.command;

import java.util.UUID;

public record WorkspaceCreateCommand(
        @jakarta.validation.constraints.NotBlank
        @jakarta.validation.constraints.Size(max = 100)
        String name,

        @jakarta.validation.constraints.NotNull
        UUID ownerPublicId
) {}
