package io.app.stacktodobe.workspace.command.support;

import io.app.stacktodobe.workspace.application.command.WorkspaceCreateCommand;

import java.util.concurrent.ThreadLocalRandom;

public final class WorkspaceFixtures {

    public static WorkspaceCreateCommand validCreateCommand(Long ownerId) {
        return new WorkspaceCreateCommand(validName(), ownerId);
    }

    public static String validName() {
        String random = Long.toString(ThreadLocalRandom.current().nextLong(Long.MAX_VALUE), 16); // 0-9a-z
        String name = "ws-" + random;
        return name.length() <= 100 ? name : name.substring(0, 100);
    }
}

