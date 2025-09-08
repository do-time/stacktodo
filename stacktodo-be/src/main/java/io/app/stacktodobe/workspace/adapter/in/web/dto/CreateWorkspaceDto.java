package io.app.stacktodobe.workspace.adapter.in.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateWorkspaceDto(
        @NotBlank String name,
        @NotNull UUID ownerId
) {
}
