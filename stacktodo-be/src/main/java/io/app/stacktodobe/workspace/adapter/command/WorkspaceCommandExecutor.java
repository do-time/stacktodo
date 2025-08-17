package io.app.stacktodobe.workspace.adapter.command;

import io.app.stacktodobe.workspace.persistence.entity.Workspace;
import io.app.stacktodobe.workspace.presentation.command.WorkspaceCreateCommand;
import lombok.RequiredArgsConstructor;

import java.util.function.Consumer;

@RequiredArgsConstructor
public class WorkspaceCommandExecutor {

    private final Consumer<Workspace> saveWorkspace;

    public void execute(WorkspaceCreateCommand command) {
        //saveWorkspace.accept(Workspace.createWorkspace(command));
    }

    public void validate(WorkspaceCreateCommand command) {
        //
    }

}
