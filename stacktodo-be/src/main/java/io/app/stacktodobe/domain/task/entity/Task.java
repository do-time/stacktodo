package io.app.stacktodobe.domain.task.entity;

import io.app.stacktodobe.domain.base.BaseEntity;
import io.app.stacktodobe.domain.category.entity.Category;
import io.app.stacktodobe.domain.member.entity.Member;
import io.app.stacktodobe.domain.task.eums.TaskStatus;
import io.app.stacktodobe.domain.workspace.entity.Workspace;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "tasks")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@EntityListeners(AuditingEntityListener.class)
public class Task extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    // 템플릿에서 복사된 경우
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id")
    private Category template;

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
