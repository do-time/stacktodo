package io.app.stacktodobe.task.domain.usecase;

import io.app.stacktodobe.category.persistence.entity.Category;
import io.app.stacktodobe.category.persistence.repository.CategoryRepository;
import io.app.stacktodobe.member.adapter.out.persistence.entity.MemberEntity;
import io.app.stacktodobe.member.adapter.out.persistence.repository.MemberRepository;
import io.app.stacktodobe.task.adapter.model.InvalidCommandException;
import io.app.stacktodobe.task.persistence.repository.TaskRepository;
import io.app.stacktodobe.task.presentation.command.TaskCreateCommand;
import io.app.stacktodobe.workspace.adapter.out.persistence.entity.WorkspaceEntity;
import io.app.stacktodobe.workspace.adapter.out.persistence.repository.WorkspaceRepository;
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
        MemberEntity owner = memberRepository.findById(command.ownerId())
                .orElseThrow(() -> new InvalidCommandException("존재하지 않는 사용자입니다: " + command.ownerId()));

        WorkspaceEntity workspaceEntity = workspaceRepository.findById(command.workspaceId())
                .orElseThrow(() -> new InvalidCommandException("존재하지 않는 워크스페이스입니다: " + command.workspaceId()));

        Category category = categoryRepository.findById(command.categoryId())
                .orElseThrow(() -> new InvalidCommandException("존재하지 않는 워크스페이스입니다: " + command.workspaceId()));

        // 2) 저장
//        taskRepository.save(Task.createTask(command, workspace, category, owner));

//        Category category = null;
//        if (command.categoryId() != null) {
//            category = categoryRepository.findById(command.categoryId())
//                    .orElseThrow(() -> new InvalidCommandException("존재하지 않는 categoryId: " + command.categoryId()));
//        }
    }
}
