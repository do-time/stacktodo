package io.app.stacktodobe.workspace.command.controller;

import io.app.stacktodobe.member.persistence.repository.MemberRepository;
import io.app.stacktodobe.member.support.MemberFixtures;
import io.app.stacktodobe.utils.E2eTest;
import io.app.stacktodobe.workspace.command.support.WorkspaceFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@E2eTest
class Workspaces_POST_Specs {
    @Test
    @DisplayName("Work 생성 정상 요청시 204 NO_CONTENT")
    void create_returns_204(
            @Autowired TestRestTemplate testRestTemplate,
            @Autowired MemberRepository memberRepository
    ){
        Long ownerId = MemberFixtures.persistedMemberId(memberRepository);
        var command = WorkspaceFixtures.validCreateCommand(ownerId);

        var response = testRestTemplate.postForEntity(
                "/api/v1/workspaces/create-workspace",
                command,
                Void.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(204);
    }
}