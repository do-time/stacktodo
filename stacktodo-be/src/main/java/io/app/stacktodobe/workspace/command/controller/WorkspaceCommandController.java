package io.app.stacktodobe.workspace.command.controller;

import io.app.stacktodobe.workspace.domain.usecase.WorkspaceCommandUserCase;
import io.app.stacktodobe.workspace.presentation.command.WorkspaceCreateCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/workspaces")
public class WorkspaceCommandController {
    private final WorkspaceCommandUserCase workspaceCommandUserCase;

    @PostMapping("/create-workspace")
    public ResponseEntity<?> createWorkspace(@RequestBody WorkspaceCreateCommand command) {
        workspaceCommandUserCase.createWorkspace(command);
        return ResponseEntity.noContent().build();
    }
}
