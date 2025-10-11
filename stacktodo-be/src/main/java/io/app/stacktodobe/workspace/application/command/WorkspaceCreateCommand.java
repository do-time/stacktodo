package io.app.stacktodobe.workspace.application.command;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.UUID;

@Builder
public record WorkspaceCreateCommand(
        @NotBlank
        @Size(max = 100)
        String name,

        @NotNull
        UUID ownerId
) {
    public static WorkspaceCreateCommand of(String name, UUID ownerId) {
        return WorkspaceCreateCommand.builder()
                .name(name)
                .ownerId(ownerId)
                .build();
    }
}
