package io.app.stacktodobe.task.adapter.out.persistence.entity;

import io.app.stacktodobe.category.persistence.entity.Category;
import io.app.stacktodobe.common.entity.BaseEntity;
import io.app.stacktodobe.member.adapter.out.persistence.entity.MemberEntity;
import io.app.stacktodobe.task.domain.model.TaskStatus;
import io.app.stacktodobe.workspace.adapter.out.persistence.entity.WorkspaceEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "tasks")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@EntityListeners(AuditingEntityListener.class)
public class TaskEntity extends BaseEntity {
    @Column(name = "task_id", unique = true, nullable = false)
    private UUID taskId;

    @Column(length = 255, nullable = false)
    private String title;

    @Lob
    private String description;

    @Column(name = "workspace_id", nullable = false)
    private UUID workspaceId;

    @Column(name = "category_id")
    private UUID categoryId;

    // 담당자(소유자)
    @Column(name = "owner_id")
    private UUID ownerId;

    // 템플릿에서 복사된 경우
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "template_id")
//    private Category template;

    private LocalDate dueDate;
    private LocalTime dueTime;

    @Column(nullable = false)
    @Builder.Default
    private Integer priority = 3; // 1~5

    @Min(0) @Max(100)
    @Column(nullable = false)
    @Builder.Default
    private Integer percentComplete = 0;

    @Column(nullable = false)
    @Builder.Default
    private boolean isRoutine = false;

    // iCal RRULE 또는 JSON 문자열
    @Column(length = 255)
    private String recurrenceRule;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private TaskStatus status = TaskStatus.PENDING;

}
