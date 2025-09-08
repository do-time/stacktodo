package io.app.stacktodobe.workspace.command;

import io.app.stacktodobe.member.support.MemberFixtures;
import io.app.stacktodobe.utils.E2eTest;
import io.app.stacktodobe.workspace.adapter.in.web.dto.CreateWorkspaceDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;


import java.util.UUID;

import static io.app.stacktodobe.utils.TestSourceGenerator.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@E2eTest
class POST_Specs {

    @Test
    @DisplayName("Workspace 생성 정상 요청시 204 NO_CONTENT")
    void create_returns_204(@Autowired MemberFixtures memberFixtures, @Autowired TestRestTemplate testRestTemplate){
        //arrange
        UUID ownerId = memberFixtures.createMemberAndGetMemberId();
        var dto = new CreateWorkspaceDto(generateWorkspaceName(), ownerId);

        //act
        var response = testRestTemplate.postForEntity(
                "/api/v1/workspaces/create-workspace",
                dto,
                Void.class);

        //assert
        assertThat(response.getStatusCode().value()).isEqualTo(204);
    }

    @Test
    @DisplayName("존재하지 않는 ownerId로 Workspace 생성 요청시 404 NOT_FOUND")
    void create_withNonExistingOwner_returns_404(@Autowired TestRestTemplate testRestTemplate) {
        //arrange
        UUID nonExistingOwnerId = UUID.randomUUID();

        var dto = new CreateWorkspaceDto(generateWorkspaceName(), nonExistingOwnerId);

        //act
        var response = testRestTemplate.postForEntity(
                "/api/v1/workspaces/create-workspace",
                dto,
                String.class
        );

        //assert
        assertThat(response.getStatusCode().value()).isEqualTo(404);
    }


    @Test
    @DisplayName("중복된 이름의 Workspace 생성 요청시 409 CONFLICT")
    void create_withDuplicateName_returns_409(@Autowired MemberFixtures memberFixtures, @Autowired TestRestTemplate testRestTemplate) {
        //arrange
        UUID ownerId = memberFixtures.createMemberAndGetMemberId();
        var dto = new CreateWorkspaceDto(generateWorkspaceName(), ownerId);

        //act
        // 첫 번째 워크스페이스 생성 (정상)
        var firstResponse = testRestTemplate.postForEntity(
                "/api/v1/workspaces/create-workspace",
                dto,
                Void.class
        );
        assertThat(firstResponse.getStatusCode().value()).isEqualTo(204);

        // 같은 이름으로 다시 생성 요청
        var duplicateResponse = testRestTemplate.postForEntity(
                "/api/v1/workspaces/create-workspace",
                dto,
                String.class
        );

        //assert
        assertThat(duplicateResponse.getStatusCode().value()).isEqualTo(409);
    }
}