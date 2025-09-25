package io.app.stacktodobe.workspace.adapter.in.web.controller;

import io.app.stacktodobe.workspace.adapter.in.web.dto.WorkspaceView;
import io.app.stacktodobe.workspace.application.port.in.WorkspaceQueryUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/workspaces")
@Slf4j
public class WorkspaceQueryController {
    private final WorkspaceQueryUseCase workspaceQueryUseCase;

    @GetMapping("/{workspaceId}")
    public ResponseEntity<WorkspaceView> getWorkspace(
            @PathVariable UUID workspaceId, Principal principal) {
        //member id 검증 추가 해야함. memberid와 name으로 중복 체크.
        UUID memberId = UUID.fromString(principal.getName());

        WorkspaceView view = workspaceQueryUseCase.getById(workspaceId, memberId);
        return ResponseEntity.ok(view);
    }

    @GetMapping("/by-name/{name}")
    public ResponseEntity<WorkspaceView> getByName(
            @PathVariable String name, Principal principal) {
        //member id 검증 추가 해야함. memberid와 name으로 중복 체크.
        UUID memberId = UUID.fromString(principal.getName());

        WorkspaceView view = workspaceQueryUseCase.getByName(name, memberId);
        return ResponseEntity.ok(view);
    }

    @GetMapping("/by-owner/{ownerId}")
    public ResponseEntity<List<WorkspaceView>> listByOwner(
            @PathVariable UUID ownerId) {
        //member id 검증 추가 해야함. memberid와 name으로 중복 체크.
        List<WorkspaceView> viewList = workspaceQueryUseCase.listByOwnerId(ownerId);
        return ResponseEntity.ok(viewList);
    }
}
