package io.app.stacktodobe.workspace.application.service;


import io.app.stacktodobe.workspace.adapter.in.web.dto.WorkspaceView;
import io.app.stacktodobe.workspace.application.port.in.WorkspaceQueryUseCase;
import io.app.stacktodobe.workspace.application.port.out.WorkspaceQueryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class WorkspaceQueryService implements WorkspaceQueryUseCase {
    private final WorkspaceQueryPort workspaceQueryPort;

    @Override
    public WorkspaceView getWorkspace(UUID workspaceId, UUID memberId) {
        return workspaceQueryPort.findById(workspaceId, memberId);
    }

    @Override
    public WorkspaceView getById(UUID workspaceId,  UUID memberId) {
        return workspaceQueryPort.findById(workspaceId, memberId);
    }

    @Override
    public WorkspaceView getByName(String name,  UUID memberId) {
        return workspaceQueryPort.findByName(name, memberId);
    }

    @Override
    public List<WorkspaceView> listByOwnerId(UUID ownerId) {
        return workspaceQueryPort.findAllByOwnerId(ownerId);
    }
}
