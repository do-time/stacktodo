package io.app.stacktodobe.workspace.command;

import io.app.stacktodobe.member.support.MemberFixtures;
import io.app.stacktodobe.utils.E2eTest;
import io.app.stacktodobe.workspace.adapter.in.web.dto.CreateWorkspaceDto;
import io.app.stacktodobe.workspace.adapter.in.web.dto.WorkspaceView;
import io.app.stacktodobe.workspace.command.support.WorkspaceFixtures;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static io.app.stacktodobe.utils.TestSourceGenerator.generateWorkspaceName;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@E2eTest
class GET_Specs {

    @Test
    @DisplayName("Workspace 조회 정상 요청시 200 OK")
    void getWorkspace_returns_200(@Autowired WorkspaceFixtures workspaceFixtures){
        //arrange
        String name = generateWorkspaceName();
        workspaceFixtures.createWorkspace(name);

        //act
        ResponseEntity<WorkspaceView> response = workspaceFixtures.getClient().getForEntity(
                "/api/v1/workspaces/{name}",
                WorkspaceView.class,
                name
        );

        //assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().name()).isEqualTo(name);
    }
}