package io.app.stacktodobe.workspace.adapter.in.web.controller;

import io.app.stacktodobe.workspace.adapter.in.web.dto.WorkspaceView;
import io.app.stacktodobe.workspace.application.port.in.WorkspaceQueryUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/workspaces")
@Slf4j
public class WorkspaceQueryController {
    private final WorkspaceQueryUseCase workspaceQueryUseCase;

    @GetMapping("/{name}")
    public ResponseEntity<WorkspaceView> getWorkspace(
            @PathVariable String name) {
        //member id 검증 추가 해야함. memberid와 name으로 중복 체크.
        WorkspaceView workspaceView = workspaceQueryUseCase.getWorkspace(name);
        return ResponseEntity.ok(workspaceView);
    }
}
