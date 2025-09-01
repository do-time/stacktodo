package io.app.stacktodobe.workspace.domain.model;

import java.util.UUID;

public record  Workspace(
        UUID workspaceId,
        String name,
        UUID ownerPublicId
){}
