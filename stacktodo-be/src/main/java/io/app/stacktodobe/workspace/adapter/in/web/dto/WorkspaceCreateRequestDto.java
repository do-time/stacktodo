package io.app.stacktodobe.workspace.adapter.in.web.dto;

import java.util.UUID;

public record WorkspaceCreateRequestDto(
        @jakarta.validation.constraints.NotBlank String name,
        @jakarta.validation.constraints.NotNull UUID ownerPublicId
) {
}
