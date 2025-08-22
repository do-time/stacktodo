package io.app.stacktodobe.task.persistence.entity;

import io.app.stacktodobe.common.entity.BaseEntity;
import io.app.stacktodobe.category.persistence.entity.Category;
import io.app.stacktodobe.member.persistence.entity.Member;
import io.app.stacktodobe.task.presentation.command.TaskCreateCommand;
import io.app.stacktodobe.tasklist.persistence.entity.TaskList;
import io.app.stacktodobe.workspace.persistence.entity.Workspace;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "tasks")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Builder(access = AccessLevel.PRIVATE)
public class Task extends BaseEntity {

    @Column(length = 255, nullable = false)
    private String title;

    @Lob
    private String description;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "workspace_id", nullable = false)
    private Workspace workspace;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    // 담당자(소유자)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private Member owner;

    // 템플릿에서 복사된 경우, 역추적용
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "original_task_id")
    private Task originalTaskId;

    private LocalDate dueDate;
    private LocalTime dueTime;

    @Column(nullable = false)
    @Builder.Default
    private Integer priority = 3; // 1~5

    @Column(nullable = false)
    @Builder.Default
    private boolean isComplete = false;

    @Column(nullable = false)
    @Builder.Default
    private boolean isRoutine = false;

    @Column(length = 255)
    private String recurrenceRule; // 반복 규칙. 추후 Google 캘린더 연동을 위해 String type으로 구현

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private TaskStatus status = TaskStatus.PENDING;

    public static Task createTask(TaskCreateCommand command, Workspace workspace, Category category, Member owner){
        return Task.builder()
                .title(command.title())
                .description(command.description())
                .workspace(workspace)
                .category(category)
                .owner(owner)
                .originalTaskId(null)
                .dueDate(command.dueDate())
                .dueTime(command.dueTime())
                .priority(command.priority())
                .isComplete(command.isComplete())
                .build();
    };
}
