package io.app.stacktodobe.task.support;

import io.app.stacktodobe.category.persistence.entity.Category;
import io.app.stacktodobe.category.persistence.entity.CategoryScope;
import io.app.stacktodobe.category.persistence.repository.CategoryRepository;
import io.app.stacktodobe.member.persistence.entity.Member;
import io.app.stacktodobe.member.persistence.repository.MemberRepository;
import io.app.stacktodobe.task.presentation.command.TaskCreateCommand;
import io.app.stacktodobe.workspace.persistence.entity.Workspace;
import io.app.stacktodobe.workspace.persistence.repository.WorkspaceRepository;
import io.app.stacktodobe.workspace.presentation.command.WorkspaceCreateCommand;

import java.util.concurrent.ThreadLocalRandom;

public final class TaskFixtures {

    public static TaskCreateCommand validCreateCommand(Long workspaceId, Long categoryId, Long ownerId) {
        return new TaskCreateCommand(validTitle(), null, workspaceId, categoryId, ownerId, null, null, null, 3, false, false, null);
    }

    // categoryFixtures 로 이동해야 함.
    public static Category persistedCategory(CategoryRepository categoryRepository,
                                             Workspace workspace,
                                             String name){
        Category category = Category.builder()
                .name(name)
                .scope(CategoryScope.WORKSPACE)
                .workspace(workspace)
                .build();
        return categoryRepository.save(category);
    }

    public static String validTitle() {
        String random = Long.toString(ThreadLocalRandom.current().nextLong(Long.MAX_VALUE), 16); // 0-9a-z
        String name = "task-" + random;
        return name.length() <= 255 ? name : name.substring(0, 255);
    }
}

