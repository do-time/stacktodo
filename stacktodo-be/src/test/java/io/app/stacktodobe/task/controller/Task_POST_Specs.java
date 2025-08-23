package io.app.stacktodobe.task.controller;

import io.app.stacktodobe.category.persistence.entity.Category;
import io.app.stacktodobe.category.persistence.repository.CategoryRepository;
import io.app.stacktodobe.member.persistence.entity.Member;
import io.app.stacktodobe.member.persistence.repository.MemberRepository;
import io.app.stacktodobe.member.support.MemberFixtures;
import io.app.stacktodobe.task.persistence.repository.TaskRepository;
import io.app.stacktodobe.task.support.TaskFixtures;
import io.app.stacktodobe.utils.E2eTest;
import io.app.stacktodobe.workspace.command.support.WorkspaceFixtures;
import io.app.stacktodobe.workspace.persistence.entity.Workspace;
import io.app.stacktodobe.workspace.persistence.repository.WorkspaceRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@E2eTest
class Task_POST_Specs {
    private static final String TASKS_CREATE_URL = "/api/v1/tasks/create";

    @Test
    @DisplayName("Task 생성 - 정상 요청이면 204")
    void create_returns_204(
            @Autowired TestRestTemplate testRestTemplate,
            @Autowired MemberRepository memberRepository,
            @Autowired TaskRepository taskRepository,
            @Autowired WorkspaceRepository workspaceRepository,
            @Autowired CategoryRepository categoryRepository
            ){
        Member member = MemberFixtures.persisted(memberRepository);
        Workspace workspace = WorkspaceFixtures.persisted(workspaceRepository, memberRepository, member.getId());
        Category category = TaskFixtures.persistedCategory(categoryRepository, workspace, "운동");

        System.out.println("멤버 = " + member.toString());
        System.out.println("워크스페이스 = " + workspace.toString());
        System.out.println("카테고리 = " + category.toString());

        var command = TaskFixtures.validCreateCommand(workspace.getId(), category.getId(), member.getId());

        var response = testRestTemplate.postForEntity(
                TASKS_CREATE_URL,
                command,
                Void.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(204);
    }
}