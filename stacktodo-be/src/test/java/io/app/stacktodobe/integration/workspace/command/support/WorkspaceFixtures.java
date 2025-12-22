package io.app.stacktodobe.integration.workspace.command.support;


import io.app.stacktodobe.integration.member.support.MemberFixtures;
import io.app.stacktodobe.workspace.adapter.in.web.dto.CreateWorkspaceDto;
import io.app.stacktodobe.workspace.adapter.in.web.dto.WorkspaceView;
import io.app.stacktodobe.workspace.application.command.WorkspaceCreateCommand;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static io.app.stacktodobe.utils.TestSourceGenerator.*;
import static java.util.Objects.requireNonNull;

public final class WorkspaceFixtures {
    private final MemberFixtures memberFixtures;

    private WorkspaceFixtures(MemberFixtures memberFixtures) {
        this.memberFixtures = memberFixtures;
    }

    public TestRestTemplate getClient() {
        return memberFixtures.getClient();
    }

    public WorkspaceCreateCommand validCreateCommand(UUID ownerPublicId) {
        return new WorkspaceCreateCommand(generateWorkspaceName(), ownerPublicId);
    }

    public UUID createWorkspaceAndGetId() {
        String name = generateWorkspaceName();

        createWorkspace(name);

        ResponseEntity<WorkspaceView> response = getClient().getForEntity(
                "/api/v1/workspaces/{name}",
                WorkspaceView.class,
                name
        );

        return requireNonNull(response.getBody()).workspaceId();
    }

    public void createWorkspace(String name) {
        UUID ownerId = memberFixtures.createMemberAndGetMemberId();
        var dto = new CreateWorkspaceDto(name, ownerId);

        getClient().postForEntity(
                "/api/v1/workspaces",
                dto,
                Void.class);
    }

    public static WorkspaceFixtures create(MemberFixtures memberFixtures) {
        return new WorkspaceFixtures(memberFixtures);
    }
}

