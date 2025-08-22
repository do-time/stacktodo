package io.app.stacktodobe.task.domain.usecase;

import io.app.stacktodobe.category.persistence.entity.Category;
import io.app.stacktodobe.category.persistence.repository.CategoryRepository;
import io.app.stacktodobe.member.persistence.entity.Member;
import io.app.stacktodobe.member.persistence.repository.MemberRepository;
import io.app.stacktodobe.task.persistence.entity.Task;
import io.app.stacktodobe.task.persistence.repository.TaskRepository;
import io.app.stacktodobe.task.presentation.command.TaskCreateCommand;
import io.app.stacktodobe.workspace.adapter.model.InvalidCommandException;
import io.app.stacktodobe.workspace.persistence.entity.Workspace;
import io.app.stacktodobe.workspace.persistence.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskCommandService implements TaskCommandUsecase{
    private final TaskRepository taskRepository;

    private final MemberRepository memberRepository;

    private final WorkspaceRepository workspaceRepository;

    private final CategoryRepository categoryRepository;
    @Override
    public void createTask(TaskCreateCommand command) {
        // 1) 연관 엔티티 검증
        Member owner = memberRepository.findById(command.ownerId())
                .orElseThrow(() -> new InvalidCommandException("존재하지 않는 사용자입니다: " + command.ownerId()));

        Workspace workspace = workspaceRepository.findById(command.workspaceId())
                .orElseThrow(() -> new InvalidCommandException("존재하지 않는 워크스페이스입니다: " + command.workspaceId()));

        Category category = categoryRepository.findById(command.categoryId())
                .orElseThrow(() -> new InvalidCommandException("존재하지 않는 워크스페이스입니다: " + command.workspaceId()));

        // 2) 저장
        taskRepository.save(Task.createTask(command, workspace, category, owner));

//        Category category = null;
//        if (command.categoryId() != null) {
//            category = categoryRepository.findById(command.categoryId())
//                    .orElseThrow(() -> new InvalidCommandException("존재하지 않는 categoryId: " + command.categoryId()));
//        }
    }
}
