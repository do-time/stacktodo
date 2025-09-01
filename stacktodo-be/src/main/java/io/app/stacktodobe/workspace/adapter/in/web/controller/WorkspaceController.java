package io.app.stacktodobe.workspace.adapter.in.web.controller;

import io.app.stacktodobe.workspace.adapter.in.web.dto.WorkspaceCreateRequestDto;
import io.app.stacktodobe.workspace.application.port.in.WorkspaceUseCase;
import io.app.stacktodobe.workspace.application.command.WorkspaceCreateCommand;
import io.app.stacktodobe.workspace.mapper.WorkspaceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/workspaces")
public class WorkspaceController {
    private final WorkspaceUseCase workspaceUseCase;

    @PostMapping("/create-workspace")
    public ResponseEntity<?> create(@RequestBody WorkspaceCreateRequestDto dto) {
        WorkspaceCreateCommand command = WorkspaceMapper.toCreateCommand(dto);
        workspaceUseCase.create(command);
        return ResponseEntity.noContent().build();
    }
}
