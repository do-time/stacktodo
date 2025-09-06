package io.app.stacktodobe.workspace.command.support;


import io.app.stacktodobe.workspace.application.command.WorkspaceCreateCommand;
import org.springframework.boot.test.web.client.TestRestTemplate;

import java.util.UUID;

import static io.app.stacktodobe.utils.TestSourceGenerator.*;


public final class WorkspaceFixtures {
    private final TestRestTemplate client;

    private WorkspaceFixtures(TestRestTemplate client) {
        this.client = client;
    }

    public TestRestTemplate getClient() {
        return client;
    }
    public WorkspaceCreateCommand validCreateCommand(UUID ownerPublicId) {
        return new WorkspaceCreateCommand(generateWorkspaceName(), ownerPublicId);
    }
}

